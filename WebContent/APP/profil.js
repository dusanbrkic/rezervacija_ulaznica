Vue.component("Profil", {
    data: function () {
        return {
        	korisnik: {
        		ime: "",
            	prezime: "",
            	username: "",
            	password: "",
            	pol: "",
            	datumRodjenja: "",
            	brojBodova : 0,
            	tip: "",
        	},
            cookie: "",
            role: "",
        }
    },

    template: `

      <div id="register-div">
      <link rel="stylesheet" href="CSS/register.css" type="text/css">
      <h1 id="h1-register">Promena profila</h1>
      <form @submit.prevent="submitChange">
        <table id="reg-table">
          <tr>
            <td>Ime:</td>
            <td><input type="text" v-model="korisnik.ime"></td>
          </tr>
          <tr>
            <td>Prezime:</td>
            <td><input type="text" v-model="korisnik.prezime"></td>
          </tr>
          <tr>
            <td>Korisničko ime:</td>
            <td><input type="text" v-model="korisnik.username" disabled></td>
          </tr>
          <tr>
            <td>Lozinka:</td>
            <td><input type="password" v-model="korisnik.password"></td>
          </tr>
          <tr>
            <td>Pol:</td>
            <td>{{korisnik.pol}}
            </td>
          </tr>
          <tr>
            <td>Datum rođenja:</td>
            <td>{{korisnik.datumRodjenja}}</td>
          </tr>
          <tr>
          	<td>Broj bodova:</td><td>{{korisnik.brojBodova}}</td>
          </tr>
          <tr>
          	<td>Tip korisnika:</td><td>{{korisnik.tip}}</td>
          </tr>
          <tr>
            <td style="text-align: center; font-size: 30px;">
              <input v-on:click="cancel" value="Otkaži"></td>
            <td style="text-align: center; font-size: 30px;">
              <input type="submit" value="Izmeni profil"></td>
          </tr>
        </table>
      </form>
      </div>

    `
    ,
    
    mounted(){
    	
    	this.cookie = localStorage.getItem("cookie")
    	this.role = localStorage.getItem("rola");
    	axios
    	.get("rest/korisnici/getKorisnik/"+this.cookie)
    	.then(response=>{
    		this.korisnik = response.data
			this.korisnik.datumRodjenja = moment(String(response.data.datumRodjenja)).format("DD/MM/YYYY HH:mm")
    		console.log(this.korisnik)
    	})
    	
    },
    methods :{
        submitChange : async function () {
        	
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
            let korisnik = {
                ime: this.korisnik.ime,
                prezime: this.korisnik.prezime,
                password: this.korisnik.password, 
            }
            console.log(korisnik);
            await axios
                .post("rest/korisnici/izmeniProfil/"+this.cookie, korisnik)

            this.$router.push("")
        },
        cancel: function () {
            this.$router.push("")
        }
    }


});