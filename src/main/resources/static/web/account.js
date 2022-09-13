const { createApp } = Vue


createApp({
    data() {
        return {
            transactions: [],
            accounts: [],
            id: '',
            params: '',
            queryString: '',
            fromDate: "",
            toDate: "",
            accountNumber: "",
        }
    },
    created() {
        this.queryString = location.search;
        this.params = new URLSearchParams(this.queryString);
        this.id = this.params.get('id');
        this.loadData()
    },
    mounted() {

    },
    methods: {
        loadData() {
            axios.get('/api/accounts/' + this.id)
                .then(response => {
                    this.accounts = response.data;
                    this.transactions = this.accounts.transactions.sort((a, b) => b.id - a.id)
                    this.shortenDate(this.transactions)
                    this.accountNumber = this.accounts.number

                })
        },
        shortenDate(arrayTransactions) {
            arrayTransactions.forEach(transaction => {
                transaction.date = transaction.date.slice(0, 10)
            });
        },
        logout() {
            axios.post('/api/logout')
                .then(() => window.location.href = "./index.html")
        },
        printTransaction() {
            this.fromDate = new Date(this.fromDate).toISOString()
            this.toDate = new Date(this.toDate).toISOString()
            axios.post("/api/transactions/filtered", { fromDate: `${this.fromDate}`, thruDate: `${this.toDate}`, accountNumber: `${this.accountNumber}` })
            .then(response => {
                swal({
                    title: "Good job!",
                    text: "Data downloaded, check your desktop!",
                    icon: "success",
                    button: "Close",
                });
            })
            .catch(error => swal(error.response.data))
        },
    },
    computed: {

    },
}).mount('#app')

