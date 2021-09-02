Vue.component("HomeProdavac", {
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
        <li style="float: left;"><a class="navbar-option" href="#/prodavac">Manifestacije</a></li>
        <li style="float: left;"><a class="navbar-option" href="#/prodavac/registrujManifestaciju">Registruj manifestaciju</a></li>
        <li style="float: right;"><a class="navbar-option" href="#/prodavac/login">Odjavi se</a></li>
        <li style="float: right;"><a class="navbar-option" href="#/prodavac/profil">Moj Profil</a></li>
      </ul>

      <router-view/>
      </div>
    `
    ,
    methods: {}
});