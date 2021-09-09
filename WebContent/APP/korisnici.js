Vue.component("Korisnici", {
    data: function () {
        return {
            korisnici: [],
            cookie: "",
            rola: "PRODAVAC",
            ime: '',
            prezime: '',
            korisnickoIme: '',
            brojBodova: '',
            checkedUloge: ["KUPCI", "PRODAVCI"],
            checkedTipovi: ["BRONZANI", "SREBRNI", "ZLATNI"],
            sort1: "IME", //IME PREZIME USERNAME BROJ_BODOVA
            sort2: "ASC",
        }
    },
    mounted() {
        this.cookie = localStorage.getItem("cookie")

        this.search()

    },
    template: `
      <div>
      <link rel="stylesheet" href="CSS/korisnici.css" type="text/css">
      <div id="filter-korisnici-container">
        <form @submit.prevent="search" id="filter-korisnici-div">
          <input id="ime-pretraga" type="text" placeholder="Ime.." v-model="ime">
          <input id="prezime-pretraga" type="text" placeholder="Prezime.." v-model="prezime">
          <input id="kIme-pretraga" type="text" placeholder="Korisnicko ime.." v-model="korisnickoIme">
          <div id="tip-check1">
            <label style="display: block;">
              <input v-model="checkedTipovi" type="checkbox" checked="checked" value="BRONZANI">
              Bronzani
            </label>
            <label style="display: block;">
              <input v-model="checkedTipovi" type="checkbox" checked="checked" value="SREBRNI">
              Srebrni
            </label>
            <label style="display: block;">
              <input type="checkbox" checked="checked" v-model="checkedTipovi" value="ZLATNI">
              Zlatni
            </label>
          </div>
          <div v-if="rola==='ADMIN'" id="tip-check2">
            <label style="display: block;">
              <input type="checkbox" checked="checked" v-model="checkedUloge" value="KUPCI">
              Kupci
            </label>
            <label style="display: block;">
              <input type="checkbox" checked="checked" v-model="checkedUloge" value="PRODAVCI">
              Prodavci
            </label>
          </div>
          <div id="sort-option1">
            <select v-model="sort1">
              <option disabled value="">Soritaj po:</option>
              <option value="IME">Ime</option>
              <option value="PREZIME">Prezime</option>
              <option value="USERNAME">Korisnicko ime</option>
              <option value="BODOVI">Bodovi</option>
            </select>
          </div>
          <div id="sort-option2">
            <select v-model="sort2">
              <option value="DESC">Opadajuce</option>
              <option value="ASC">Rastuce</option>
            </select>
          </div>
          <div id="search-button-div" style="display: grid; place-items: center;">
            <input type="submit" value="Pretrazi">
          </div>
        </form>
      </div>

      <div id="korisnici-div">
        <div class="row" v-for="k in korisnici">
          <div class="card">
            <div id="ime">{{ k.ime }}</div>
            <div id="prezime">{{ k.prezime }}</div>
            <div id="korisnickoIme">{{ k.username }}</div>
            <div id="broj-bodova" v-if="k.brojBodova">{{ k.brojBodova }}</div>
            <div id="tip" v-if="k.tip">{{ k.tip }}</div>
            <div id="uloza">{{ k.uloga }}</div>
            <button id="ukloni-btn">Ukloni korisnika</button>
          </div>
        </div>
      </div>
      </div>
    `
    ,
    methods: {
        search: function () {
            let opcijePretrage = {
                params: {
                    ime: this.ime,
                    prezime: this.prezime,
                    username: this.korisnickoIme,
                    kupci: this.checkedUloge.includes("KUPCI"),
                    zaposleni: this.checkedUloge.includes("PRODAVCI"),
                    sortat: this.sort1 + this.sort2,
                }
            }
            axios.post("rest/korisnici/getKorisnici/" + this.cookie, this.checkedTipovi, opcijePretrage)
                .then(response => {
                    this.korisnici.length = 0
                    this.korisnici = response.data
                })
        }
    }
});