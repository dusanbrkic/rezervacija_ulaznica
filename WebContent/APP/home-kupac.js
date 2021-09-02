Vue.component("HomeKupac", {
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
        <li style="float: left;"><a class="navbar-option" href="#/kupac">Manifestacije</a></li>
        <li style="float: right;"><a class="navbar-option" href="#/kupac/login">Odjavi se</a></li>
        <li style="float: right;"><a class="navbar-option" href="#/kupac/profil">Moj Profil</a></li>
      </ul>

      <router-view/>
      </div>
    `
    ,
    methods: {}
});