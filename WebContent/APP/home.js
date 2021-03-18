Vue.component("Home",{
	data: function () {
	    return {
	      
	    }
	},
	    
	    template : ` 
	<div>
		Dobrodosli u najjacu aplikaciju za rezervaciju ulaznica na Balkanu!
		
		<div>
			<button v-on:click="loginKorisnik">
				Login Page
			</button>
			<button v-on:click="registerKupac">
				Register Page
			</button>
		</div>
		<div>
		
		</div>
			
	</div>		  
`
	,
	methods : {
		loginKorisnik : function() {
			app.$router.push("/login")
		},
		registerKupac : function() {
			app.$router.push("/register")
		}
	}
});