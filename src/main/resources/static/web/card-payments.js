const { createApp } = Vue


createApp({
    data() {
        return {
            client: [],
            accounts: [],
            accountsActive: "",
            id: '',
            params: '',
            queryString: '',
            loans: [],
            cards: [],
            cardsActive: [],
            cardNumber: "",
            fechaActual: [],
            cardHolder: "",
            thruDate: "",
            cvv: "",
            amount: "",
            description: "",
            accountNumber: "",
        }

    },
    created() {
        this.queryString = location.search;
        this.params = new URLSearchParams(this.queryString);
        this.id = this.params.get('id');
        this.loadData()
        this.fechaActual = new Date(Date.now()).toISOString().slice(2, 7);
    },
    mounted() {

    },
    methods: {
        loadData() {
            axios.get('/api/client/current')
                .then(response => {
                    this.client = response.data;
                    this.accounts = this.client.accounts;
                    this.accountsActive = this.accounts.filter(a => a.accountActive)
                    this.loans = this.client.loans;
                    this.cards = this.client.cards.sort((a, b) => a.id - b.id);
                    this.cardsActive = this.cards.filter(card => card.cardActive)
                    this.normalizeCardsDate(this.cards)

                })
        },

        normalizeCardsDate(arrayCards) {
            arrayCards.forEach(card => {
                card.thruDate = card.thruDate.slice(2, 7)
            });
        },
        deleteCard() {
            axios.patch('/api/client/current/cards', `number=${this.cardNumber}`)
                .then(() =>
                    window.location.reload())
        },
        logout() {
            axios.post('/api/logout')
                .then(() => window.location.href = "./index.html")
        },
        payWithCard() {
            swal("Are you sure?",{
                buttons: {
                    cancel: true,
                    confirm: true,
                },
            }).then((result) => {
                if (result) {
                    axios.post('/api/transactions/payment', {cardNumber:this.cardNumber,cvv:this.cvv,amount:this.amount,description:this.description,thruDate:this.thruDate,cardHolder:this.cardHolder,accountNumber:this.accountNumber})
                        .then(() => swal("Payment successful"))
                        .catch(error => swal(`${error.response.data}`))
                        .then(setTimeout(function(){ window.location.href = "./accounts.html"},2500))
                    }
                })


    },
},
    computed: {

},
}).mount('#app')


