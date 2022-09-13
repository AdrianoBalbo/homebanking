const { createApp } = Vue


createApp({
    data() {
        return {
            client: [],
            accounts: [],
            loans: [],
            cards: [],
            cardType: [],
            cardColor: [],
        }

    },
    created() {
        this.loadData()
    },
    mounted() {

    },
    methods: {
        loadData() {
            axios.get('/api/client/current')
                .then(response => {
                    this.client = response.data;
                    this.accounts = this.client.accounts;
                    this.loans = this.client.loans;
                    this.cards = this.client.cards.sort((a,b)=> a.id-b.id);
                })
        },
        logout(){
            axios.post('/api/logout')
            .then(()=> window.location.href="./index.html")
        },
        createCard(){
            axios.post('/api/client/current/cards',`cardColor=${this.cardColor}&cardType=${this.cardType}`,{headers:{'content-type':'application/x-www-form-urlencoded'}})
            .then(r=>window.location.href="./cards.html")
            .catch(error=> 
                swal(error.response.data) )
        },
    },
    computed: {

    },
}).mount('#app')



