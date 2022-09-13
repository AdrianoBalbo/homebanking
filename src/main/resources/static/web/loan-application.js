const { createApp } = Vue


createApp({
    data() {
        return {
            client: [],
            accounts: [],
            loans: [],
            loanType: [],
            payments: "",
            loanCar: {},
            loanMortgage: {},
            loanPersonal: {},
            destinyAccount: "",
            loanAmount: "",
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
                    this.accountsActive = this.accounts.filter(a => a.accountActive)

                })
            this.loadLoans()
            console.log(this.loans);

        },
        loadLoans() {
            axios.get('/api/loans')
                .then(response => {
                    this.loans = response.data
                    this.loanCar = this.loans.filter(loan => loan.name == "Car")
                    this.loanMortgage = this.loans.filter(loan => loan.name == "Mortgage")
                    this.loanPersonal = this.loans.filter(loan => loan.name == "Personal")
                })
        },
        logout() {
            axios.post('/api/logout')
                .then(() => window.location.href = "./index.html")
        },
        createLoan() {
            swal("Are you sure you want to do this?", {
                buttons: true,
            })
                .then(res => {
                    if (res) {
                        axios.post('/api/loans', { amount: this.loanAmount, payments: this.payments, destinyAccountNumber: this.destinyAccount, loanName: this.loanType })
                            .then(r => window.location.href = "./accounts.html")
                            .catch(error =>
                                swal(error.response.data))
                    }
                })
        },
    },
    computed: {
        interestedLoan() {
            let amountToPay = this.loanAmount
            let payPerPayment
            if (this.loanType == "Mortgage") { //Mortgage Loan
                if (this.payments == 12) {
                    amountToPay = amountToPay * 1.20
                } else if (this.payments == 24) {
                    amountToPay = amountToPay * 1.25
                } else if (this.payments == 36) {
                    amountToPay = amountToPay * 1.30
                } else if (this.payments == 48) {
                    amountToPay = amountToPay * 1.35
                } else if (this.payments == 60) {
                    amountToPay = amountToPay * 1.40
                }
            } else if (this.loanType == "Personal") { //Personal Loan
                if (this.payments == 6) {
                    amountToPay = amountToPay * 1.20
                } else if (this.payments == 12) {
                    amountToPay = amountToPay * 1.25
                } else if (this.payments == 24) {
                    amountToPay = amountToPay * 1.30
                }
            } else if (this.loanType == "Car") { //Car Loan
                if (this.payments == 6) {
                    amountToPay = amountToPay * 1.20
                } else if (this.payments == 12) {
                    amountToPay = amountToPay * 1.25
                } else if (this.payments == 24) {
                    amountToPay = amountToPay * 1.30
                } else if (this.payments == 36) {
                    amountToPay = amountToPay * 1.35
                }
            }

            payPerPayment = amountToPay / this.payments
            return Math.round(amountToPay) + " in " + this.payments + " fees of $" + Math.round(payPerPayment)
        },

    },
}).mount('#app')





