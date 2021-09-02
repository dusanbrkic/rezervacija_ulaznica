Vue.component("Home", {
    data: function () {
        return {

        }
    },
    mounted() {
    },
    template: `
      <div>
      <link rel="stylesheet" href="CSS/home.css" type="text/css">
      <link rel="stylesheet" href="CSS/index.css" type="text/css">
      <ul class="navbar">
        <li style="float: left;"><a class="navbar-option" href="#/">Manifestacije</a></li>
        <li style="float: right;"><a class="navbar-option" href="#/login">Prijavi se</a></li>
        <li style="float: right;"><a class="navbar-option" href="#/register">Registruj se</a></li>
      </ul>
      
      <router-view/>
      </div>
    `
    ,
    methods: {

    }
});