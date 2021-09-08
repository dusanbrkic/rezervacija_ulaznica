Vue.component("HomeAdmin", {
    data: function () {
        return {}
    },
    mounted() {
    },
    template: `
      <div>
      <link rel="stylesheet" href="CSS/home.css" type="text/css">
      <link rel="stylesheet" href="CSS/index.css" type="text/css">
      <ul class="navbar">
        <li style="float: left;"><a class="navbar-option" href="#/admin">Manifestacije</a></li>
        <li style="float: left;"><a class="navbar-option" href="#/admin/korisnici">Svi korisnici</a></li>
        <li style="float: left;"><a class="navbar-option" href="#/admin/karte">Sve karte</a></li>
        <li style="float: left;"><a class="navbar-option" href="#/admin/odobravanjeManifestacije">Odobravanje Manifestacija</a></li>
        <li style="float: left;"><a class="navbar-option" href="#/admin/registrujProdavca">Registruj Prodavca</a></li>
        <li style="float: right;"><a class="navbar-option" v-on:click="logOut">Odjavi se</a></li>
        <li style="float: right;"><a class="navbar-option" href="#/admin/profil">Moj Profil</a></li>
      </ul>

      <router-view/>
      </div>
    `
    ,
    methods: {
        logOut: function () {
            localStorage.clear()
            app.$router.push("/")
        }
    }
});