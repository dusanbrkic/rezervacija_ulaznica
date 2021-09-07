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
            komentari: []
        }
    },
    mounted() {
        this.cookie = localStorage.getItem("cookie");
        this.idManifestacije = this.$route.params.id
        this.getManifestacija();
        this.ucitajKomentare();
    },
    template: `
      <div>
      <link rel="stylesheet" href="CSS/manifestacija.css" type="text/css">
      <div id="manifestacija-div-container">
        <div id="manifestacija-div">
          <div id="naziv">{{ manifestacija.naziv }}</div>
          <div id="tip">{{ manifestacija.tip }}</div>
          <div id="prBrojKarata">{{ manifestacija.brojSlobodnihMesta }}</div>
          <div id="datum">{{ manifestacija.vremeOdrzavanja }}</div>
          <div id="cena">{{ manifestacija.regularCena }}</div>
          <div id="status">{{ manifestacija.aktivna }}</div>
          <div id="lokacija">{{ manifestacija.lokacija.adresa }} {{ manifestacija.lokacija.grad }}</div>
          <div id="poster" style="width: 350px; height: 400px">
            <img :src="manifestacija.poster" alt="poster"
                 style="height: auto;max-width: 100%;max-height: 100%"> <!-- ./RES/slicice/posteri/exit_poster.jpg -->
          </div>
        </div>
      </div>

      <div class="row" v-for="k in komentari">
        <div class="card">
          <div id="usn-kupac">{{ k.kupac }}</div>
          <div id="tekst-komentara">
            <textarea disabled>
              {{ k.tekst }}
            </textarea>
          </div>
        </div>
      </div>

      <div class="row">
        <div class="card">
          <div id="usn-kupac">Ostavite svoj komentar:</div>
          <div id="tekst-komentara">
            <textarea>
            </textarea>
          </div>
        </div>
      </div>
      </div>
    `
    ,
    methods: {
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

    }
});