// validate required parameters
if (!context.partyId) {
    ec.message.addError("partyId is required")
}

// by this the details can not be an empty spaces
context.groupName = context.groupName?.trim()
context.description  = context.description?.trim()

if (!context.groupName) {
    ec.message.addError("firstName is required")
    return
}
if (!context.description) {
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
    return
}

// it will make empty entity object
def partyGroup = ec.entity.makeValue("party.PartyGroup")
partyGroup.setFields(context, true, null, null)

//  Create Person
partyGroup.create()

// return response
responseMessage = "Party Group ${partyGroup.groupName} ${partyGroup.description} created successfully!"