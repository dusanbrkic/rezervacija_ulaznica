Vue.component("Karte", {
    data: function () {
        return {
            karte: [],
            cookie: "",
            maxPriceKarta: 0,
            minPriceKarta: Infinity,
            manifestacija: '',
            filterovaneManifestacije: [{id: 0, naziv: 'kucajte da biste pretrazili...'}],
            checkedTipovi: ["REGULAR", "FAN_PIT", "VIP"],
            checkedStatus: "SVE", //SVE REZERVISANA ODUSTANAK
            sort1: "NAZIV", //NAZIV CENA VREME
            sort2: "ASC",
            rola: "",
        }
    },
    mounted() {
        this.cookie = localStorage.getItem("cookie")
        this.rola = "KUPAC";
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
      <link rel="stylesheet" href="CSS/karte.css" type="text/css">
      <div id="filter-karte-container">
        <form @submit.prevent="search(false)" id="filter-karte-div">
          <input list="manifestacije-lista" id="manifestacija-pretraga" type="text" 
                 placeholder="Odaberi manifestaciju.." v-model="manifestacija" v-on:keypress="filterujManifestacije">
          <datalist id="manifestacije-lista">
            <option v-for="m of filterovaneManifestacije" :value="m.id">{{m.naziv}}</option>
          </datalist>
          
          <div id="cena-pretraga">
            <p>
              <label for="amount">Cena:</label>
              <input type="text" id="amount" readonly style="border:0; color:#f6931f; font-weight:bold;">
            </p>
            <div id="slider-range"></div>
          </div>
          <div id="tip-check">
            <label style="display: block;">
              <input v-model="checkedTipovi" type="checkbox" checked="checked" value="REGULAR">
              Regular karte
            </label>
            <label style="display: block;">
              <input v-model="checkedTipovi" type="checkbox" checked="checked" value="FAN_PIT">
              Fan Pit karte
            </label>
            <label style="display: block;">
              <input type="checkbox" checked="checked" v-model="checkedTipovi" value="VIP">
              Vip karte
            </label>
          </div>
          <div id="status-div">
            <label style="display: block;">
              <input type="radio" checked="checked" name="status" v-model="checkedStatus" value="SVE">
              Sve
            </label>
            <label style="display: block;">
              <input type="radio" name="status" v-model="checkedStatus" value="ODUSTANAK">
              Odustanci
            </label>
            <label style="display: block;">
              <input type="radio" name="status" v-model="checkedStatus" value="REZERVISANA">
              Rezervisane
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
              <option value="VREME">Vreme</option>
              <option value="CENA">Cena</option>
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

      <div id="karte-div">
        <div class="row" v-for="k in karte">
          <div class="card">
            <div id="naziv">{{ k.manifestacija.naziv }}</div>
            <div id="tip">{{ k.tip }}</div>
            <div id="datum">{{ k.manifestacija.vremeOdrzavanja }}</div>
            <div id="poster" style="width: 150px; height: 200px">
              <img :src="k.manifestacija.poster" alt="poster"
                   style="height: auto;width: 100%; max-height: 200px"> <!-- ./RES/slicice/posteri/exit_poster.jpg -->
            </div>
            <div id="cena" style="display: table; height:100%; overflow: hidden;">
              <div style="display: table-cell; vertical-align: middle; text-align: center;">{{ k.cena }}</div>
            </div>
            <div id="status" style="">{{ k.status }}</div>
          </div>
        </div>
      </div>
      </div>
    `
    ,
    methods: {
        search: function (initial) {
            let opcijePretrage = {
                params: {
                    naziv: this.manifestacija,
                    datumod: initial ? null : new Date($('input[name="datetimes"]').data('daterangepicker').startDate),
                    datumdo: initial ? null : new Date($('input[name="datetimes"]').data('daterangepicker').endDate),
                    cenaod: initial ? 0 : $("#slider-range").slider("values", 0),
                    cenado: initial ? Infinity : $("#slider-range").slider("values", 1),
                    statusKarte: initial ? null : this.statusKarte,
                    sortat: this.sort1 + this.sort2
                }
            }
            if(this.rola === "KUPAC") {
                axios.post("rest/karte/getMojeKarte/" + this.cookie, this.checkedTipovi, opcijePretrage)
                    .then(response => {
                        this.karte.length = 0
                        for (let k of response.data) {
                            axios.get('rest/manifestacije/getManifestacija/' + k.manifestacija)
                                .then(response => {
                                    k.manifestacija = response.data
                                })
                            if (initial) {
                                if (k.cena > this.maxPriceKarta)
                                    this.maxPriceKarta = k.cena
                                if (k.cena < this.minPriceKarta)
                                    this.minPriceKarta = k.cena
                            }
                        }
                        if (initial) {
                            $("#slider-range").slider("option", "max", this.maxPriceKarta);
                            $("#slider-range").slider("option", "min", this.minPriceKarta);
                            $("#slider-range").slider("option", "values", [this.minPriceKarta, this.maxPriceKarta]);
                            $("#amount").val($("#slider-range").slider("values", 0) +
                                " RSD - " + $("#slider-range").slider("values", 1) + " RSD");
                        }
                        this.karte = response.data
                    })
            }
        },
        filterujManifestacije: function () {
            let opcijePretrage = {
                params: {
                    naziv: this.manifestacija,
                    lokacija: '',
                    datumod: null,
                    datumdo: null,
                    lokacijaGd: '',
                    cenaod: 0,
                    cenado: Infinity,
                    rasprodate: "SVE",
                    sortat: 'NAZIVASC'
                }
            }
            axios.post("rest/manifestacije/getManifestacije", ["KONCERT", "POZORISTE", "FESTIVAL", "OSTALO"], opcijePretrage)
                .then(response => {
                    this.filterovaneManifestacije = response.data
                })
        }
    }
});