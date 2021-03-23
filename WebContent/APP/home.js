Vue.component("Home", {
    data: function () {
        return {
            cookieToken: "",
            rola: ""
        }
    },
    mounted() {
        let storage = localStorage.getItem("cookie")
        if (storage != null) {
            this.cookieToken = localStorage.getItem("cookie")
        }
        if (this.cookieToken!=="")
            this.validateUser()
    },
    template: `
      <div>
      Dobrodosli u najjacu aplikaciju za rezervaciju ulaznica na Balkanu!

      <div>
        <button v-on:click="loginKorisnik">
          Login Page
        </button>
        <button v-on:click="registerKupac">
          Register Page
        </button>
      </div>
      <div v-if="rola==='KUPAC'">
        Znam da si kupac! tvoj token je {{ cookieToken }}
        <button>
          Ulogovani balata kupac akcija
        </button>
      </div>
      <div v-else-if="rola==='ADMIN'">
        Znam da si admin!
        <button>
          Ulogovani balata admin akcija
        </button>
      </div>
      <div v-else-if="rola==='PRODAVAC'">
        Znam da si prodavcojec!
        <button>
          Ulogovani balata prodavac akcija
        </button>
      </div>
      </div>
    `
    ,
    methods: {
        loginKorisnik: function () {
            app.$router.push("/login")
        },
        registerKupac: function () {
            app.$router.push("/register")
        },
        validateUser: function () {
            axios
                .get("rest/korisnici/validateUser/" + this.cookieToken)
                .then(response => (this.rola = response.data))
        }
    }
});