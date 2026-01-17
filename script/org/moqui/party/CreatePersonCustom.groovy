// we will validate required parameters
if (!partyId) {
    ec.message.addError("partyId is required")
}
if (!firstName) {
    ec.message.addError("firstName is required")
}
if (!lastName) {
    ec.message.addError("lastName is required")
}