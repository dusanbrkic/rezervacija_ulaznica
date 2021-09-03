Vue.component("ManifestacijaForma", {
    data: function () {
        return {
            naziv: "",
            tip: "",
            brojMesta: "",
            datum: "",
            cena: "",
            statusManifestacije: "",
            lokacija: "",
            poster: "",
            grad: "",
        }
    },

    template: `

      <div id="register-div">
      <link rel="stylesheet" href="CSS/register.css" type="text/css">
      <h1 id="h1-register">Registracija manifestacije</h1>
      <form>
        <table id="reg-table">
          <tr>
            <td>Naziv:</td>
            <td><input type="text" v-model="naziv"></td>
          </tr>
          <tr>
            <td>Tip manifestacije:</td>
            <td>
              <select>
                <option disabled value="">Tip manifestacije:</option>
                <option value="0">Koncert</option>
                <option value="1">Pozoriste</option>
                <option value="2">Festival</option>
                <option value="3">Ostalo</option>
              </select>
            </td>
          </tr>
          <tr>
            <td>Broj mesta:</td>
            <td><input type="number" v-model="brojMesta"></td>
          </tr>
          <tr>
            <td>Datum odrzavanja:</td>
            <td><input type="date" v-model="datum"></td>
          </tr>
          <tr>
            <td>Cena:</td>
            <td><input type="number" v-model="cena">RSD</td>
          </tr>
          <tr>
            <td>Lokacija:</td>
            <td><input type="text" v-model="lokacija"></td>
          </tr>
          <tr>
            <td>Grad:</td>
            <td><input type="text" v-model="grad"></td>
          </tr>
          <tr>
            <td>
              Izaberite poster manifestacije:
            </td>
            <td>
              <input type="file"
                     id="poster_input" name="poster"
                     accept="image/png, image/jpeg">
            </td>
          </tr>
          <tr>
            <td style="text-align: center; font-size: 30px;">
              <input type="submit" v-on:click="cancel" value="Otkaži"></td>
            <td style="text-align: center; font-size: 30px;">
              <input type="submit" v-on:click="submit" value="Registruj manifestaciju"></td>
          </tr>
        </table>
      </form>
      </div>

    `
    ,
    methods: {
        submit: async function () {
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
            await axios
                .post("rest/manifestacije/registracijaManifestacije", kupac)
                .then((response) => (this.cookie = response.data))

            if (this.cookie == "") {
                alert("Korisnicko ime zauzeto!")
                return;
            } else {
                localStorage.setItem("cookie", this.cookie)
            }

            this.$router.push("/")
        },
        cancel: function () {
            this.$router.push("/")
        }
    }


});