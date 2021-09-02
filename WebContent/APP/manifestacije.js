Vue.component("Manifestacije", {
    data: function () {
        return {
            cookie: ""
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
        <div id="filter-manifestacije-div">
          <input id="naziv-pretraga" type="text" placeholder="Naziv.."/>
          <input id="lokacija-pretraga" type="text" placeholder="Lokacija.."/>
          <input id="grad-pretraga" type="text" placeholder="Grad.."/>
          <div id="cena-pretraga">
            <p>
              <label for="amount">Cena:</label>
              <input type="text" id="amount" readonly style="border:0; color:#f6931f; font-weight:bold;">
            </p>
            <div id="slider-range"></div>
          </div>
          <div id="tip-check1">
            <label style="display: block;">
              <input type="checkbox" checked="checked">
              Koncert
            </label>
            <label style="display: block;">
              <input type="checkbox" checked="checked">
              Pozoriste
            </label>
          </div>
          <div id="tip-check2">
            <label style="display: block;">
              <input type="checkbox" checked="checked">
              Festival
            </label>
            <label style="display: block;">
              <input type="checkbox" checked="checked">
              Ostalo
            </label>
          </div>
          <div id="rasprodate-div">
            <label style="display: block;">
              <input type="radio" checked="checked" name="rasprodate">
              Sve
            </label>
            <label style="display: block;">
              <input type="radio" name="rasprodate">
              Nerasprodate
            </label>
            <label style="display: block;">
              <input type="radio" name="rasprodate">
              Rasprodate
            </label>
          </div>
          <div style="display:inline-block;margin-right:20px;" id="datum-pretraga">
            <label for="datetimerange">Datum i vreme:</label>
            <input id="datetimerange" type="text" name="datetimes"/>
          </div>
          <div id="sort-option1">
            <select>
              <option disabled value="">Soritaj po:</option>
              <option value="0">Naziv</option>
              <option value="1">Datum</option>
              <option value="2">Cena</option>
              <option value="3">Lokacija</option>
            </select>
          </div>
          <div id="sort-option2">
            <select>
              <option value="0">Opadajuce</option>
              <option value="1">Rastuce</option>
            </select>
          </div>
          <div id="search-button-div" style="display: grid; place-items: center;">
            <button>Pretrazi</button>
          </div>

        </div>
      </div>
      <div id="manifestacije-div">
        <link rel="stylesheet" href="CSS/manifestacije.css" type="text/css">
        <div class="row" v-for="i in [1,2,3,4,5]">
          <div class="card">
            <div id="naziv"><a v-on:click="pregledManifestacije" style="color: blue; cursor: pointer;">Naziv</a></div>
            <div id="tip">Tip manifestacije</div>
            <div id="datum">datum i vreme</div>
            <div id="lokacija">lokacija</div>
            <div id="poster" style="width: 150px; height: 200px">
              <img src="./RES/slicice/posteri/exit_poster.jpg" alt="poster"
                   style="height: auto;width: 100%; max-height: 200px">
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
        }
    }
});