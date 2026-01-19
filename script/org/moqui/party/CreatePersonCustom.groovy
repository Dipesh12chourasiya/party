// validate required parameters
if (!context.partyId) {
    ec.message.addError("partyId is required")
}

context.firstName = context.firstName?.trim()
context.lastName  = context.lastName?.trim()

if (!context.firstName) {
    ec.message.addError("firstName is required")
    return
}
if (!context.lastName) {
    ec.message.addError("lastName is required")
    return
}

// if validation errors exist, by this it stop execution
if (ec.message.hasError()) return

// check if Party exists
def party = ec.entity.find("party.Party")
        .condition("partyId", context.partyId)
        .one()

if (!party) {
    ec.message.addError("Party with partyId ${partyId} does not exist")
    return // fixed the context issue bug
}

// from docs
//def tutorial = ec.entity.makeValue("tutorial.Tutorial")
//tutorial.setFields(context, true, null, null)
//if (!tutorial.tutorialId) tutorial.setSequencedIdPrimary()
//tutorial.create()

// prepare data for Person creation
// it will make empty entity object
def person = ec.entity.makeValue("party.Person")
person.setFields(context, true, null, null)

//  Create Person
person.create()

// return response
responseMessage = "Person ${person.firstName} ${person.lastName} created successfully!"