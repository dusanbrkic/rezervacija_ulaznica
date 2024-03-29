Vue.component("Manifestacija", {
    data: function () {
        return {
            manifestacija: {
                vremeOdrzavanja: '',
                naziv: '',
                tip: '',
                brojSlobodnihMesta: '',
                aktivna: '',
                regularCena: '',
                poster: '',
                lokacija: {
                    adresa: '',
                    grad: ''
                },
            },
            idManifestacije: '',
            cookie: '',
            komentari: [],
            rola: '',
        }
    },
    mounted() {
        this.cookie = localStorage.getItem("cookie");
        this.idManifestacije = this.$route.params.id
        this.getManifestacija();
        this.ucitajKomentare();
        this.role = localStorage.getItem("rola");
        var rola = this.role;
        if(this.role=="KUPAC"){
        	$("#kupovina").css('display','block');
        }
        this.getProdavac()
        if(this.role=="PRODAVAC" && this.prodavac===this.manifestacija.prodavac){
        	$("#izmeni").css('display','block');
        }
        if(this.role=="ADMIN"){
        	$("#obrisi").css('display','block');
        }
    },
    template: `
      <div>
      <link rel="stylesheet" href="CSS/manifestacija.css" type="text/css">
      <div id="manifestacija-div-container">
        <div id="manifestacija-div">
          <div id="naziv">{{ manifestacija.naziv }}</div>
          <div id="tip">{{ manifestacija.tip }}</div>
          <div id="prBrojKarata">Broj slobodni mesta: {{ manifestacija.brojSlobodnihMesta }}/{{manifestacija.brojMesta}}</div>
          <div id="datum">{{ manifestacija.vremeOdrzavanja }}</div>
          <div id="cena">{{ manifestacija.regularCena }}</div>
          <div id="status">{{ manifestacija.aktivna }}</div>
          <div id="lokacija">{{ manifestacija.lokacija.adresa }} {{ manifestacija.lokacija.grad }}</div>
          <div id="kupi">
          	<table id="kupovina" style="display:none;">
          	<tr>Regular<input id="regular" style="width:5%;" type="number" min="0" step="1" value="0"/>
          		Fan pit<input id="pit" style="width:5%;" type="number" min="0" step="1" value="0" />
          		VIP<input id="vip" style="width:5%;" type="number" min="0" step="1" value="0"/></tr>
          	<tr><button v-on:click="proveriCenu(manifestacija.id)">Proveri cenu</button><button v-on:click="rezervisiKarte(manifestacija.id)">Rezervisi karte</button></tr>
          	<tr id="ukupnacena">Ukupna cena je: 0</tr>
          	</table>
          	<div id="izmeni" style="display:none;">
          		<button v-on:click="izmeniManifestaciju()">Izmeni manifestaciju</button>
          	</div>
          	<div id="obrisi" style="display:none;">
          		<button v-on:click="obrisiManifestaciju(manifestacija)">Obriši manifestaciju</button>
          	</div>
          </div>
          <div id="poster" style="width: 350px; height: 400px">
            <img :src="manifestacija.poster" alt="poster"
                 style="height: auto;max-width: 100%;max-height: 100%"> <!-- ./RES/slicice/posteri/exit_poster.jpg -->
          </div>
        </div>
      </div>

      <div class="row" v-for="k in komentari">
        <div class="card">
          <div id="usn-kupac">{{ k.kupac }}</div>
          <div id="tekst-komentara" style="align-self: stretch">
            <textarea style="align-self: stretch; resize: none;" disabled readonly>
              {{ k.tekst }}
            </textarea>
          </div>
        </div>
      </div>

      <div class="row">
        <div class="card">
          <div id="usn-kupac">Ostavite svoj komentar:</div>
          <div id="tekst-komentara" style="align-self: stretch">
            <textarea style="align-self: stretch; resize: none; width: 100%; height: 100%;">
            </textarea>
          </div>
        </div>
      </div>
      </div>
    `
    ,
    methods: {
        getProdavac: function (){
            axios.get('rest/korisnici/getKorisnik/' + this.cookie)
                .then(response => (this.prodavac = response.data.username))
        },
    	izmeniManifestaciju: function(manifestacija){
            localStorage.setItem("unetaManifestacija", this.manifestacija.id)
    		app.$router.push('registrujManifestaciju')
    	},
    	obrisiManifestaciju: function(manifestacija){
    		if(confirm("Da li ste sigurni da zelite da obrisete manifestaciju")){
    		console.log("brisanje");
    		axios
    		.delete("rest/manifestacije/obrisiManifestaciju/"+this.cookie+"/"+manifestacija.id)
    		.then(reponse=>{
    			alert("Manifestacija uspesno obrisana")
    			location.reload()
    		})
    		}
    	},
    	
        getManifestacija: function () {
            axios.get('rest/manifestacije/getManifestacija/' + this.idManifestacije)
                .then(response => {
                    this.manifestacija = response.data
                    this.manifestacija.vremeOdrzavanja = moment(String(
                        new Date(this.manifestacija.vremeOdrzavanja))).format("DD/MM/YYYY HH:mm")
                })

        },

        ucitajKomentare: function () {
            axios.get('rest/manifestacije/getKomentari/' + this.idManifestacije + '/' + this.cookie)
                .then(response => {
                    this.komentari = response.data
                })
        },
        proveriCenu: async function(manifestacija){
        	let reg = $("#regular").val();
        	let pit = $("#pit").val();
        	let vip = $("#vip").val();
        	let opcije = {
                    params: {
                        regular: reg,
                        fan: pit,
                        vip: vip,
                        
                    }
                }
        	let cena = 0;
        	 await axios
        	.get('rest/karte/proveriCenu/'+this.cookie+'/'+manifestacija, opcije)
        	.then(response =>{
        		cena = response.data;
        	})
        	$("#ukupnacena").html("Ukupna cena je: "+cena);
        	
        },
        rezervisiKarte : async function(manifestacija){
        	let reg = $("#regular").val();
        	let pit = $("#pit").val();
        	let vip = $("#vip").val();
        	let opcije = {
                    params: {
                        regular: reg,
                        fan: pit,
                        vip: vip,
                        
                    }
                }
        	await axios
        	.post('rest/karte/rezervisiKarte/'+this.cookie+'/'+manifestacija, null, opcije)
        	.then(response=>{
                this.getManifestacija();
        		alert("Uspešno rezervisane karte!");
        	})
        	
        },

    }
});