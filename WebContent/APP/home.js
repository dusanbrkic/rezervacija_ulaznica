Vue.component("Home",{
	data: function () {
	    return {
	    	cookieToken : "",
			rola : ""
	    }
	},
	mounted(){
		let storage = localStorage.getItem("cookie")
		if (storage != null) {
			this.cookieToken = localStorage.getItem("cookie")
		}
		console.log("Primio sam " + this.cookieToken)
	},
	beforeDestroy(){console.log("bf destroy " + this.cookieToken)},
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
		<template v-if="cookieToken!=''">
			<div v-if="validateKupac()==='true'">
				Znam da si kupac! tvoj token je {{cookieToken}}
				<button >
					Ulogovani balata kupac akcija
				</button>
			</div>
			<div v-else-if="validateAdmin()">
				Znam da si admin!
				<button >
					Ulogovani balata admin akcija
				</button>
			</div>
			<div v-else-if="validateProdavac()">
				Znam da si prodavcojec!
				<button >
					Ulogovani balata prodavac akcija
				</button>
			</div>
		</template>
	</div>		  
`
	,
	methods : {
		loginKorisnik : function() {
			app.$router.push("/login")
		},
		registerKupac : function() {
			app.$router.push("/register")
		},
		validateKupac : function (){
			let ret = false
			console.log("Saljem serveru: " + this.cookieToken)
			let cookie = this.cookieToken
			axios
				.post("rest/korisnici/validateKupac/" + this.cookieToken )
				.then(response => (ret = response.data))
			return ret
		},
		validateAdmin : function (){
			let ret = false
			axios
				.post("rest/korisnici/validateAdmin")
				.then(response => (ret = response.data))
			return ret
		},
		validateProdavac : function (){
			let ret = false
			axios
				.post("rest/korisnici/validateProdavac")
				.then(response => (ret = response.data))
			return ret
		}
	}
});