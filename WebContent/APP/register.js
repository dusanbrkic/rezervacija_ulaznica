Vue.component("Register",{
	data: function () {
	    return {    
	    }
	},
	    
	    template : ` 
    
<div id="register-div">
<link rel="stylesheet" href="CSS/register.css" type="text/css">
	    	<h1 id="h1-register">Registracija</h1>
		<form>
		<table id="reg-table">
	    	<tr><td>Ime:</td><td><input type="text" name="ime"></td></tr>
	    	<tr><td>Prezime:</td><td><input type="text" name="prezime"></td></tr>
	    	<tr><td>Korisničko ime:</td><td><input type="text" name="username"></td></tr>
	    	<tr><td>Lozinka:</td><td><input type="password" name="password"></td></tr>
	    	<tr><td>Pol:</td><td><select><option value="M">Muški</option><option value="Z">Ženski</option></select></td></tr>
	    	<tr><td>Datum rođenja:</td><td><input type="date" name="datumr"></td></tr>
	    	<tr><td colspan="2" style="text-align: center; font-size: 30px;"><input type="submit" value="Registruj se"></td></tr>
		</table>
		</form>
		
</div>
	  
`
	,
	methods : {
		init : function() {
			
		}
	}
		

});