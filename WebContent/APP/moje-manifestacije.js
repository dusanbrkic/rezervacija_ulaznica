Vue.component("MojeManifestacije", {
    data: function () {
        return {
            cookie: "",
            manifestacije: [],
        }
    },

    mounted(){
      this.cookie = localStorage.getItem("cookie");
      this.dobaviManifestacije();
    },

    template: `

<div>
<div style="margin-bottom: 15px"></div>

      <div id="manifestacije-div">
        <link rel="stylesheet" href="CSS/manifestacije.css" type="text/css">
        <div class="row" v-for="m in manifestacije">
          <div class="card">
            <div id="naziv"><a v-on:click="pregledManifestacije" style="color: blue; cursor: pointer;">{{ m.naziv }}</a>
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
    	dobaviManifestacije:  function () {
    		 axios
    		.get("rest/manifestacije/getMojeManifestacije/"+this.cookie)
    		.then(response => {
                    this.manifestacije.length = 0
                    for (let m of response.data) {
                        m.vremeOdrzavanja = new Date(m.vremeOdrzavanja)
                        m.vremeOdrzavanja = moment(String(m.vremeOdrzavanja)).format("DD/MM/YYYY HH:mm")
                       
                    }  
                    
                    this.manifestacije = response.data;
    		})
        },
    }


});