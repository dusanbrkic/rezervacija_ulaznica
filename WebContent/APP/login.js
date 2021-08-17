Vue.component("Login", {
    data: function () {
        return {
            username: "",
            password: "",
            cookie: "",
            role: ""
        }
    },

    template: `
      <div id="login-div">
      <link rel="stylesheet" href="CSS/login.css" type="text/css">
      <h1 id="h1-login">Log in</h1>
      <form @submit.prevent="login">
        <table id="reg-table">
          <tr>
            <td>Username:</td>
            <td><input type="text" v-model="username"></td>
          </tr>
          <tr>
            <td>Password:</td>
            <td><input type="password" v-model="password"></td>
          </tr>
          <tr>
            <td style="text-align: center; font-size: 30px;">
              <input type="submit" v-on:click="cancel" value="Otkaži"></td>
            <td style="text-align: center; font-size: 30px;">
              <input type="submit" v-on:click="login" value="Uloguj se"></td>
          </tr>
        </table>
      </form>
      </div>
    `
    ,
    methods: {
        cancel: function () {
            app.$router.push("/")
        },
        login: async function () {
            let user = {
                params: {
                    "username": this.username, "password": this.password
                }
            }
            await axios
                .get("rest/korisnici/loginUser", user)
                .then(response => (this.cookie = response.data))
            await axios
                .get("rest/korisnici/validateUser/" + this.cookie)
                .then(response => (this.role = response.data))
            localStorage.setItem("cookie", this.cookie)
            if (this.role === "KUPAC")
                app.$router.push("/kupac")
            else if (this.role === "ADMIN")
                app.$router.push("/admin")
            else if (this.role === "PRODAVAC")
                app.$router.push("/prodavac")
        }
    }
});