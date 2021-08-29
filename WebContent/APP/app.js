// neregistrovani korisnik
const Home = {template: '<Home></Home>'} //navbar
const Login = {template: '<Login></Login>'}
const Register = {template: '<Register></Register>'}

//kupac
const HomeKupac = {template: '<HomeKupac></HomeKupac>'} //navbar

//prodavac
// const HomeProdavac = {template: '<HomeKupac></HomeKupac>'} //navbar
// const ManifestacijaForma = {template: '<ManifestacijaForma></ManifestacijaForma>'} //dodavanje manifestacije

//admin
// const HomeAdmin = {template: '<HomeAdmin></HomeAdmin>'} //navbar
// const Korisnici =  {template: '<Korisnici></Korisnici>'}

//svi tipovi korisnika
//stranica za prikaz manifestacija je pocetna strana za sve korisnike (zadatak)
const Manifestacije = {template: '<Manifestacije></Manifestacije>'}
// const Manifestacija = {template: '<Manifestacija></Manifestacija>'}
// const Karte = {template: '<Karte></Karte>'}
// const Profil = {template: '<Profil></Profil>'}

const router = new VueRouter({
    mode: 'hash',
    routes: [
        {
            path: '/', component: Home, children: [
                {path: '', component: Manifestacije},
                {path: 'login', component: Login},
                {path: 'register', component: Register},
                // {path: 'manifestacija:id', component: Manifestacija},
            ]
        },
        {
            path: '/kupac', component: HomeKupac, children: [
                {path: '', component: Manifestacije},
                // {path: 'karte', component: Karte},
                // {path: 'profil', component: Profil},
                // {path: 'manifestacija:id', component: Manifestacija},
            ]
        },
        // {
        //     path: '/prodavac', component: HomeProdavac, children: [
        //         {path: '/', component: Manifestacije},
        //         {path: 'karte', component: Karte},
        //         {path: 'profil', component: Profil},
        //         {path: 'manifestacijaForma', component: ManifestacijaForma},
        //         {path: 'manifestacija:id', component: Manifestacija},
        //     ]
        // },
        // {
        //     path: '/admin', component: HomeAdmin, children: [
        //         {path: '/', component: Manifestacije},
        //         {path: 'profil', component: Profil},
        //         {path: 'manifestacija:id', component: Manifestacija},
        //     ]
        // },


    ]

});


var app = new Vue({
    router,
    el: '#rez-app'
})