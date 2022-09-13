const { createApp } = Vue


createApp({
    data() {
        return {
            logContainer: document.getElementById("app"),
            newFirstName: '',
            newLastName: '',
            newEmail: '',
            newPassword: '',
            userMail: '',
            userPassword: '',
        }
    },
    created() {

    },
    mounted() {

    },
    methods: {
        toggleSignUpIn() {
            this.logContainer.classList.toggle("sign-up-mode")
        },
        login() {
            axios.post('/api/login', `email=${this.userMail}&password=${this.userPassword}`, { headers: { 'content-type': 'application/x-www-form-urlencoded' } })
                .then(res => {
                    window.location.href = "./accounts.html"
                    console.log("funciona pai");
                })
                .catch(() => swal("Wrong email or password. Try again."))

        },
        register() {
            axios.post('/api/client', `firstName=${this.newFirstName}&lastName=${this.newLastName}&email=${this.newEmail}&password=${this.newPassword}`, { headers: { 'content-type': 'application/x-www-form-urlencoded' } })
                .then(res => axios.post('/api/login', `email=${this.newEmail}&password=${this.newPassword}`, { headers: { 'content-type': 'application/x-www-form-urlencoded' } })
                    .then(res => {
                        axios.post('/api/client/current/accounts/', `accountType=SAVING`, { headers: { 'content-type': 'application/x-www-form-urlencoded' } })
                        window.location.href = "./accounts.html"
                    }))
                .catch(error => swal(error.response.data))

        },
    },
    computed: {

    },
}).mount('#app')


