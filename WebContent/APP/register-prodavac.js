Vue.component("RegisterProdavac", {
    data: function () {
        return {
        	mfid: "",
            cookie: "",
            manifestacija = {
            		
            }
        }
    },

    mounted(){
      this.cookie = localStorage.getItem("cookie");
      this.mfid = localStorage.getItem("idm");
    },

    template: `

    	<div>
      

      
    	</div>

    `
    ,
    methods: {
    	
    }


});