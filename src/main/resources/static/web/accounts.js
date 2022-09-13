const { createApp } = Vue


createApp({
    data() {
        return {
            client: [],
            accounts: [],
            id: '',
            params: '',
            queryString: '',
            loans:[],
            accountsActive:"",
            accountNumber:"",
            accountType:"",
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
            axios.get('/api/client/current')
                .then(response => {
                    this.client = response.data;
                    this.accounts = this.client.accounts.sort((a,b)=> a.id-b.id);
                    this.loans = this.client.loans.sort((a,b)=> a.id-b.id);
                    this.accountsActive = this.accounts.filter(a => a.accountActive)
                    console.log(this.accountsActive);
                    console.log(this.accounts);
                })
        },
        logout(){
            axios.post('/api/logout')
            .then(()=> window.location.href="./index.html")
        },
        createAccount(){
            axios.post('/api/client/current/accounts/',`accountType=${this.accountType}`,{headers:{'content-type':'application/x-www-form-urlencoded'}})
            .then(()=> {
                window.location.reload();
                console.log("funciono padree")})
        },
        deleteAccount(){
            axios.patch("/api/client/current/accounts", `number=${this.accountNumber}`)
            .then(()=>
            window.location.reload()
            .then(()=>swal(response.data)))
            .catch(error=>swal(error.response.data))

        },
    },
    computed: {

    },
}).mount('#app')

