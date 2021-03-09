const Home  = { template: '<Home></Home>'}
const Login = { template: '<Login></Login>'}

const router = new VueRouter({
	mode : 'hash',
	routes : [
		{path: '/', component: Home },
		{path: '/login', component: Login}
		
	]
	
});


var app = new Vue({
	router,
	el: '#rez-app' 
})