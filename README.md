# secure-messaging-app
A secure Messaging app where a user can issue a ticket for another user. No user can send a message to another user if he was not issued a ticket by the receiver and this ticket is still valid. After the ticket expires, the sender doesn't have a way to send to the receiver unless the receiver issues him a new ticket.
The app ensures three main things:
  1- end to end encryption.
  2- non-repudiation.
  3- spam free. (no user can talk to you or request takling to you unless you have already issued him a ticket)
