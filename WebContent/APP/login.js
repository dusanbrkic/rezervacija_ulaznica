Vue.component("Login", {
    data: function () {
        return {}
    },

    template: `
      <div>
      Login Page
      <div>
        <button v-on:click="cancel">Cancel</button>
      </div>

      </div>
    `
    ,
    methods: {
        cancel: function () {
            app.$router.push("/")
        }
    }
});