const { createApp } = Vue


createApp({
    data() {
        return {
            client: [],
            accounts: [],
            id: '',
            params: '',
            queryString: '',
            loans: [],
            cards: [],
            cardsActive:[],
            cardNumber:"",
            fechaActual: [],
        }

    },
    created() {
        this.queryString = location.search;
        this.params = new URLSearchParams(this.queryString);
        this.id = this.params.get('id');
        this.loadData()
        this.fechaActual = new Date(Date.now()).toISOString().slice(2,7);
        console.log(this.fechaActual);
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
                    this.cardsActive = this.cards.filter(card=>card.cardActive)
                    this.normalizeCardsDate(this.cards)

                })
        },
        
        normalizeCardsDate(arrayCards){
            arrayCards.forEach(card => {
                card.thruDate = card.thruDate.slice(2,7)
            });
        },
        deleteCard(){
            axios.patch('/api/client/current/cards', `number=${this.cardNumber}`)
            .then(()=>
            window.location.reload())
        },
        logout(){
            axios.post('/api/logout')
            .then(()=> window.location.href="./index.html")
        },
    },
    computed: {

    },
}).mount('#app')


