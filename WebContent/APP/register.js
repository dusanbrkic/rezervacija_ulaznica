Vue.component("Register", {
    data: function () {
        return {
            ime: "",
            prezime: "",
            username: "",
            password: "",
            pol: "",
            datumRodjenja: "",
            cookie: ""
        }
    },

    template: `

      <div id="register-div">
      <link rel="stylesheet" href="CSS/register.css" type="text/css">
      <h1 id="h1-register">Registracija</h1>
      <form>
        <table id="reg-table">
          <tr>
            <td>Ime:</td>
            <td><input type="text" v-model="ime"></td>
          </tr>
          <tr>
            <td>Prezime:</td>
            <td><input type="text" v-model="prezime"></td>
          </tr>
          <tr>
            <td>Korisničko ime:</td>
            <td><input type="text" v-model="username"></td>
          </tr>
          <tr>
            <td>Lozinka:</td>
            <td><input type="password" v-model="password"></td>
          </tr>
          <tr>
            <td>Pol:</td>
            <td>
              <select v-model="pol">
                <option disabled value="">-- Izaberite pol --</option>
                <option value="MUSKI">Muški</option>
                <option value="ZENSKI">Ženski</option>
              </select>
            </td>
          </tr>
          <tr>
            <td>Datum rođenja:</td>
            <td><input type="date" v-model="datumRodjenja"></td>
          </tr>
          <tr>
            <td style="text-align: center; font-size: 30px;">
              <input type="submit" v-on:click="cancel" value="Otkaži"></td>
            <td style="text-align: center; font-size: 30px;">
              <input type="submit" v-on:click="submit" value="Registruj se"></td>
          </tr>
        </table>
      </form>
      </div>

    `
    ,
    methods: {
        submit: function () {
            if (this.pol === "") {
                alert("Niste uneli pol!")
                return
            }
            let kupac = {
                ime: this.ime,
                prezime: this.prezime,
                username: this.username,
                password: this.password,
                pol: this.pol,
                datumRodjenja: this.datumRodjenja
            }
            axios
                .post("rest/korisnici/registracijaKupca", kupac)
                .then((response) => (this.cookie = response.data))

            if (this.cookie == "") {
                alert("Korisnicko ime zauzeto!")
                return;
            } else {
                console.log("emitting.. " + this.cookie)
                localStorage.setItem("cookie", this.cookie)
            }

            this.$router.push("/")
        },
        cancel: function () {
            this.$router.push("/")
        }
    }


});