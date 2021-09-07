Vue.component("Manifestacije", {
    data: function () {
        return {
            manifestacije: [],
            cookie: "",
            maxPriceManifestacija: 0,
            minPriceManifestacija: Infinity,
            naziv: "",
            checkedTipovi: ["KONCERT", "POZORISTE", "FESTIVAL", "OSTALO"],
            checkedRasprodate: "SVE",
            adresa: "",
            grad: "",
            sort1: "VREME",
            sort2: "ASC",
        }
    },
    mounted() {
        this.cookie = localStorage.getItem("cookie")

        this.search(true)
        $(function () {
            $("#slider-range").slider({
                range: true,
                slide: function (event, ui) {
                    $("#amount").val(ui.values[0] + " RSD - " + ui.values[1] + " RSD");
                }
            });
            $("#amount").val($("#slider-range").slider("values", 0) +
                " RSD - " + $("#slider-range").slider("values", 1) + " RSD");
        });
        $(function () {
            $('input[name="datetimes"]').daterangepicker({
                timePicker: true,
                startDate: moment().startOf('hour'),
                endDate: moment().startOf('hour').add(32, 'hour'),
                locale: {
                    format: 'DD.M. HH:mm'
                }
            });
        });
    },
    template: `
      <div>
      <div id="filter-manifestacije-container">
        <form @submit.prevent="search(false)" id="filter-manifestacije-div">
          <input id="naziv-pretraga" type="text" placeholder="Naziv.." v-model="naziv">
          <input id="adresa-pretraga" type="text" placeholder="Lokacija.." v-model="adresa">
          <input id="grad-pretraga" type="text" placeholder="Grad.." v-model="grad">
          <div id="cena-pretraga">
            <p>
              <label for="amount">Cena:</label>
              <input type="text" id="amount" readonly style="border:0; color:#f6931f; font-weight:bold;">
            </p>
            <div id="slider-range"></div>
          </div>
          <div id="tip-check1">
            <label style="display: block;">
              <input v-model="checkedTipovi" type="checkbox" checked="checked" value="KONCERT">
              Koncert
            </label>
            <label style="display: block;">
              <input v-model="checkedTipovi" type="checkbox" checked="checked" value="POZORISTE">
              Pozoriste
            </label>
          </div>
          <div id="tip-check2">
            <label style="display: block;">
              <input type="checkbox" checked="checked" v-model="checkedTipovi" value="FESTIVAL">
              Festival
            </label>
            <label style="display: block;">
              <input type="checkbox" checked="checked" v-model="checkedTipovi" value="OSTALO">
              Ostalo
            </label>
          </div>
          <div id="rasprodate-div">
            <label style="display: block;">
              <input type="radio" checked="checked" name="rasprodate" v-model="checkedRasprodate" value="SVE">
              Sve
            </label>
            <label style="display: block;">
              <input type="radio" name="rasprodate" v-model="checkedRasprodate" value="NERASPRODATE">
              Nerasprodate
            </label>
            <label style="display: block;">
              <input type="radio" name="rasprodate" v-model="checkedRasprodate" value="RASPRODATE">
              Rasprodate
            </label>
          </div>
          <div style="display:inline-block;margin-right:20px;" id="datum-pretraga">
            <label for="datetimerange">Datum i vreme:</label>
            <input id="datetimerange" type="text" name="datetimes"/>
          </div>
          <div id="sort-option1">
            <select v-model="sort1">
              <option disabled value="">Soritaj po:</option>
              <option value="NAZIV">Naziv</option>
              <option value="VREME">Datum</option>
              <option value="CENA">Cena</option>
              <option value="LOKACIJA">Lokacija</option>
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

      <div id="manifestacije-div">
        <link rel="stylesheet" href="CSS/manifestacije.css" type="text/css">
        <div class="row" v-for="m in manifestacije">
          <div class="card">
            <div id="naziv"><a v-on:click="pregledManifestacije(m.id)" style="color: blue; cursor: pointer;">{{ m.naziv }}</a>
            </div>
            <div id="tip">{{ m.tip }}</div>
            <div id="datum">{{ m.vremeOdrzavanja }}</div>
            <div id="lokacija">{{ m.lokacija.grad }} {{ m.lokacija.adresa }}</div>
            <div id="poster" style="width: 150px; height: 200px">
              <img :src="m.poster" alt="poster"
                   style="height: auto;width: 100%; max-height: 200px"> <!-- ./RES/slicice/posteri/exit_poster.jpg -->
            </div>
            <div id="cena" style="display: table; height:100%; overflow: hidden;">
              <div style="display: table-cell; vertical-align: middle; text-align: center;">{{ m.regularCena }}</div>
            </div>
            <div id="ocena" style="">nije ocenjen</div>
          </div>
        </div>
      </div>
      </div>
    `
    ,
    methods: {
        pregledManifestacije: function (id) {
            app.$router.push('manifestacija' + id);
            localStorage.setItem("cookie", this.cookie);
        },
        search: function (initial) {
            let opcijePretrage = {
                params: {
                    naziv: this.naziv,
                    lokacija: this.adresa,
                    datumod: initial ? null : new Date($('input[name="datetimes"]').data('daterangepicker').startDate),
                    datumdo: initial ? null : new Date($('input[name="datetimes"]').data('daterangepicker').endDate),
                    lokacijaGd: this.grad,
                    cenaod: initial ? 0 : $("#slider-range").slider("values", 0),
                    cenado: initial ? Infinity : $("#slider-range").slider("values", 1),
                    rasprodate: this.checkedRasprodate, //true - samo rasprodate false - samo nerasprodate null-sve
                    sortat: this.sort1 + this.sort2
                }
            }
            axios.post("rest/manifestacije/getManifestacije", this.checkedTipovi, opcijePretrage)
                .then(response => {
                    this.manifestacije.length = 0
                    for (let m of response.data) {
                        m.vremeOdrzavanja = new Date(m.vremeOdrzavanja)
                        m.vremeOdrzavanja = moment(String(m.vremeOdrzavanja)).format("DD/MM/YYYY HH:mm")
                        if (initial) {
                            if (m.regularCena > this.maxPriceManifestacija)
                                this.maxPriceManifestacija = m.regularCena
                            if (m.regularCena < this.minPriceManifestacija)
                                this.minPriceManifestacija = m.regularCena
                        }
                    }
                    if (initial) {
                        $("#slider-range").slider("option", "max", this.maxPriceManifestacija);
                        $("#slider-range").slider("option", "min", this.minPriceManifestacija);
                        $("#slider-range").slider("option", "values", [this.minPriceManifestacija, this.maxPriceManifestacija]);
                        $("#amount").val($("#slider-range").slider("values", 0) +
                            " RSD - " + $("#slider-range").slider("values", 1) + " RSD");
                    }
                    this.manifestacije = response.data
                })
        }
    }
});