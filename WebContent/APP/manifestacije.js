Vue.component("Manifestacije", {
    data: function () {
        return {
            manifestacije: [],
            cookie: "",
            maxPriceManifestacija: null,
            minPriceManifestacija: null,
            naziv: "",
            checkedTipovi: ["KONCERT", "POZORISTE", "FESTIVAL", "OSTALO"],
            checkedRasprodate: null,
            adresa: "",
            grad: "",
            sort1: "VREME",
            sort2: "ASC",
        }
    },
    mounted() {
        this.cookie = localStorage.getItem("cookie")
        $(function () {
            $("#slider-range").slider({
                range: true,
                min: 1000,
                max: 20000,
                values: [1000, 20000],
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
        <form @submit.prevent="search" id="filter-manifestacije-div">
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
              <input type="radio" checked="checked" name="rasprodate" v-model="checkedRasprodate" value=null>
              Sve
            </label>
            <label style="display: block;">
              <input type="radio" name="rasprodate" v-model="checkedRasprodate" value=false>
              Nerasprodate
            </label>
            <label style="display: block;">
              <input type="radio" name="rasprodate" v-model="checkedRasprodate" value=true>
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
            <div id="naziv"><a v-on:click="pregledManifestacije" style="color: blue; cursor: pointer;">{m.naziv}</a>
            </div>
            <div id="tip">{m.tip}</div>
            <div id="datum">{ moment(String(m.vreme)).format("DD/MM/YYYY HH:mm") }</div>
            <div id="lokacija">{m.grad} {m.adresa}</div>
            <div id="poster" style="width: 150px; height: 200px">
            <img :src="m.poster" alt="poster"
                   style="height: auto;width: 100%; max-height: 200px"> <!-- ./RES/slicice/posteri/exit_poster.jpg -->
            </div>
            <div id="cena" style="display: table; height:100%; overflow: hidden;">
              <div style="display: table-cell; vertical-align: middle; text-align: center;">ii iii RSD</div>
            </div>
            <div id="ocena" style="">prosecna ocena</div>
          </div>
        </div>
      </div>
      </div>
    `
    ,
    methods: {
        pregledManifestacije: function () {
            $('input[name="datetimes"]').data('daterangepicker').setStartDate('03/01/2014')
        },
        search: function () {
            let opcijePretrage = {
                params: {
                    naziv: this.naziv,
                    lokacija: this.adresa,
                    datumod: new Date($('input[name="datetimes"]').data('daterangepicker').startDate),
                    datumdo: new Date($('input[name="datetimes"]').data('daterangepicker').endDate),
                    lokacijaGd: this.grad,
                    cenaod: $("#slider-range").slider("values", 0),
                    cenado: $("#slider-range").slider("values", 1),
                    tip: this.checkedTipovi,
                    rasprodate: this.checkedRasprodate, //true - samo rasprodate false - samo nerasprodate null-sve
                    sortat: this.sort1 + this.sort2
                }
            }
            axios.get("rest/manifestacije/getManifestacije", opcijePretrage)
                .then(response => {
                    this.manifestacije.clear()
                    for (let m of response.data){
                        m.vreme = new Date(m.vreme + ".000Z")
                    }
                    this.manifestacije = response.data
                })
        }
    }
});