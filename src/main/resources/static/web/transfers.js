const { createApp } = Vue


createApp({
    data() {
        return {
            client: [],
            accounts: [],
            accountFrom: "",
            accountTo: "",
            amountIngresado: "",
            descripcionIngresada: "",
            transacType: "",
            accountsActive:"",
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
                    this.accountsActive = this.accounts.filter(a=>a.accountActive)
                })
        },
        logout() {
            axios.post('/api/logout')
                .then(() => window.location.href = "./index.html")
        },
        transfer() {
            swal("Are you sure? You won't be able to recover this capital", {
                buttons: true,
            })
                .then(res => {
                    if (res) {
                        axios.post('/api/transactions', `amount=${this.amountIngresado}&description=${this.descripcionIngresada}&numberAccountFrom=${this.accountFrom}&numberAccountTo=${this.accountTo}`, {headers:{'content-type':'application/x-www-form-urlencoded'}})
                        .then(()=>window.location.href="./accounts.html")
                        .catch(error => swal(error.response.data))
                    }})
        },
    },
    computed: {

    },
}).mount('#app')

