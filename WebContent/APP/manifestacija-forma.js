Vue.component("ManifestacijaForma", {
    data: function () {
        return {
            cookie: "",
            naziv: "",
            tip: null,
            brojMesta: null,
            datum: null,
            cena: null,
            statusManifestacije: null,
            adresa: "",
            poster: "",
            grad: "",
            mId: "",
        }
    },

    mounted(){
        this.cookie = localStorage.getItem("cookie")
        this.mId  = localStorage.getItem("unetaManifestacija")
        if(this.mId)
            this.findManifestacija(this.mId)
    },

    template: `

      <div id="register-div">
      <link rel="stylesheet" href="CSS/register.css" type="text/css">
      <h1 id="h1-register">Registracija manifestacije</h1>
      <form @submit.prevent="submit">
        <table id="reg-table">
          <tr>
            <td>Naziv:</td>
            <td><input type="text" v-model="naziv"></td>
          </tr>
          <tr>
            <td>Tip manifestacije:</td>
            <td>
              <select v-model="tip">
                <option disabled value="">Tip manifestacije:</option>
                <option value="KONCERT">Koncert</option>
                <option value="POZORISTE">Pozoriste</option>
                <option value="FESTIVAL">Festival</option>
                <option value="OSTALO">Ostalo</option>
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
            <td>Adresa:</td>
            <td><input type="text" v-model="adresa"></td>
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
              <input type="file" v-on:change="encodeImgtoBase64"
                     id="poster_input" name="poster"
                     accept="image/png, image/jpeg">
            </td>
          </tr>
          <tr>
            <td style="text-align: center; font-size: 30px;">
              <input v-on:click="cancel" value="Otkaži"></td>
            <td style="text-align: center; font-size: 30px;">
              <input type="submit" value="Registruj manifestaciju"></td>
          </tr>
        </table>
      </form>
      </div>

    `
    ,
    methods: {
        findManifestacija: function (mId){
            axios.get('rest/manifestacije/getManifestacija/' + mId)
                .then(response => {
                    this.naziv = response.data.naziv
                    this.brojMesta = response.data.brojMesta
                    this.cena = response.data.regularCena
                    this.grad = response.data.lokacija.grad
                    this.adresa = response.data.lokacija.adresa
                    this.datum = new Date(response.data.vremeOdrzavanja)
                    this.tip = response.data.tip
                })
        },



        submit: async function () {
            if (this.pol === "") {
                alert("Niste uneli pol!")
                return
            }
            let manifestacija = {
                id: function makeid(length) {
                    let result           = '';
                    let characters       = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
                    let charactersLength = characters.length;
                    for ( let i = 0; i < length; i++ ) {
                        result += characters.charAt(Math.floor(Math.random() *
                            charactersLength));
                    }
                    return result;
                }(10),
                naziv: this.naziv,
                brojMesta: this.brojMesta,
                brojSlobodnihMesta: this.brojMesta,
                regularCena: this.cena,
                poster: this.poster,
                rasprodata: false,
                aktivna: true,
                deleted: false,
                tip: this.tip,
                lokacija: {
                    grad: this.grad,
                    geografskaDuzina: 0,
                    geografskaSirina: 0,
                    adresa: this.adresa,
                    deleted: false,
                },
            }
            if (!this.mId) {
                await axios
                    .post("rest/manifestacije/dodajManifestaciju/" + this.cookie, manifestacija, {params: {'vreme': new Date(this.datum)}})
                    .then((response) => (this.cookie = response.data))

                alert("Manifestacija uspesno registrovana!")
            }
            else{
            	let i = localStorage.getItem("unetaManifestacija")
                await axios
                    .post("rest/manifestacije/izmeniManifestaciju/" + this.cookie + '/' + i,
                        manifestacija, {params: {'vreme': new Date(this.datum)}})
                    .then((response) => (this.cookie = response.data))
                    alert("Manifestacija uspesno izmenjena!")
            }

            this.$router.push("/prodavac")
        },

        cancel: function () {
            this.$router.push("/")
        },

        encodeImgtoBase64: function () {
            const input = $("#poster_input")[0]
            const img = input.files[0]
            const reader = new FileReader()

            let that = this
            reader.onloadend = function () {
                that.poster =  reader.result
            }
            reader.readAsDataURL(img)
        }
    }


});