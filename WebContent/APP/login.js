Vue.component("Login",{
	data: function () {
	    return {
	      
	    }
	},
	    
	    template : ` 
<div>
		Dokrotski Login!
		
	<div>
		<button v-on:click="init">
			teraj na home
		</button>
	</div>
		
</div>		  
`
	,
	methods : {
		init : function() {
			app.$router.push("/")
		}
	}
});