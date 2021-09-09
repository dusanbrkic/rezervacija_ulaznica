Vue.component("RegisterProdavac", {
    data: function () {
        return {
            cookie: "",
        }
    },

    mounted(){
      this.cookie = localStorage.getItem("cookie");
    },

    template: `

    	<div>
      

      
    	</div>

    `
    ,
    methods: {
    	
    }


});