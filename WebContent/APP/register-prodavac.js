Vue.component("RegisterProdavac", {
    data: function () {
        return {
<<<<<<< Updated upstream
        	mfid: "",
            cookie: "",
            manifestacija : {
            		
            }
=======
            ime: "",
            prezime: "",
            username: "",
            password: "",
            pol: "",
            datumRodjenja: "",
            cookie: ""
>>>>>>> Stashed changes
        }
    },

    mounted(){
      this.cookie = localStorage.getItem("cookie");
      
    },

    template: `

    	<div id="register-div">
      <link rel="stylesheet" href="CSS/register.css" type="text/css">
      <h1 id="h1-register">Registracija</h1>
      <form @submit.prevent="submit">
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
              <input v-on:click="cancel()" value="Otkaži"></td>
            <td style="text-align: center; font-size: 30px;">
              <input type="submit" v-on:click="submit()" value="Registruj prodavca"></td>
          </tr>
        </table>
      </form>
      </div>

    `
    ,
    methods: {
    	cancel: function () {
            this.$router.push("/")
        },
    
    	submit: function(){
    		if (this.pol === "") {
                alert("Niste uneli pol!")
                return
            }
    		if(this.ime===""){
    			alert("Niste uneli ime!")
    			return
    		}
    		if(this.prezime===""){
    			alert("Niste uneli prezime!")
    			return
    		}
    		if(this.username===""){
    			alert("Niste uneli korisničko ime!")
    			return
    		}
    		if(this.password===""){
    			alert("Niste uneli lozinku!")
    			return
    		}
    			
    		let prodavac = {
                    ime: this.ime,
                    prezime: this.prezime,
                    username: this.username,
                    password: this.password,
                    pol: this.pol,
                    datumRodjenja: this.datumRodjenja
                }
    		axios
    		.post("rest/korisnici/registrujProdavca/"+this.cookie, prodavac)
    		.then(response=>{
    			if(response.data=="username"){
    				alert("Korisnicko ime je vec zauzeto!")
    			}else{
    				alert("Korisnik uspesno registrovan")
    				this.$router.push("")
    			}
    		})
    		
    	}
    }


});