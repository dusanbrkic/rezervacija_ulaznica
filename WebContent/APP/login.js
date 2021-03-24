Vue.component("Login", {
    data: function () {
        return {
            username: "",
            password: "",
            cookie: ""
        }
    },

    template: `
      <div id="login-div">
      <link rel="stylesheet" href="CSS/login.css" type="text/css">
      <h1 id="h1-login">Log in</h1>
      <form>
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
              <input type="submit" v-on:click="login" value="Log in"></td>
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
            localStorage.setItem("cookie", this.cookie)
            app.$router.push("/")
        }
    }
});