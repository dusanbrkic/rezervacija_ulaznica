const Home  = { template: '<Home></Home>'}
const Login = { template: '<Login></Login>'}
const Register = { template: '<Register></Register>'}

const router = new VueRouter({
	mode : 'hash',
	routes : [
		{path: '/', component: Home },
		{path: '/login', component: Login},
		{path: '/register', component: Register}
		
	]
	
});


var app = new Vue({
	router,
	el: '#rez-app' 
})