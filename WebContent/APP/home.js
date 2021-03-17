Vue.component("Home",{
	data: function () {
	    return {
	      
	    }
	},
	    
	    template : ` 
<div>
	Dokrotski Home!
	
	<div>
		<button v-on:click="init">
			klikni me
		</button>
	</div>
	<div>
	
	</div>
		
</div>		  
`
	,
	methods : {
		init : function() {
			app.$router.push("/Login")
		}
	}
});