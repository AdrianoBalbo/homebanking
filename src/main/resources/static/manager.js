const { createApp } = Vue


createApp({
    data() {
        return {
            clients: [],
            nameValue: '',
            lastNameValue: '',
            emailValue: '',
            id: '',
            params: '',
            queryString: '',

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
            axios.get('/api/client')
                .then(response => {
                    this.clients = response.data;
                    console.log(this.clients);
                })
        },
        addClient() {
            axios.post('/rest/clients', {
                firstName: this.nameValue,
                lastName: this.lastNameValue,
                email: this.emailValue,

            })
                .then((response) => {
                    this.clients.push(response.data)
                })
        },
        postClient() {
            axios.post('/rest/clients')
                .then(this.loadData())
        },
        deleteClient(clientSelected) {
            let link
            link = JSON.stringify(clientSelected._links.client.href)
            console.log(link)
            axios.delete(link.slice(22,-1))
            .then(response => {
                console.log(response)
                this.loadData()
            })

        },
        editClient(clientSelected) {
            let link
            link = JSON.stringify(clientSelected._links.client.href)
            let newEmail
            newEmail = prompt("Enter the new Email")
            clientSelected = {
                //No son necesarias las 2 lineas de abajo debido al uso del PATCH, si fuese PUT, seria necesario
                // firstName: clientSelected.firstName,
                // lastName: clientSelected.lastName,
                email: newEmail,
            }
            axios.patch(link.slice(22,-1), clientSelected)
            .then(this.loadData)
        },
    },
    computed: {

    },
}).mount('#app')

