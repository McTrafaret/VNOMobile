# VNO protocol reversing

Note: With some experementing I guessed that the time to render a
character in IC is about 60000 microseconds.

## Master server (AS)

It seems that the client does not read the file AS.ini in data directory(checked with procmon)
Seems like the ip is hardcoded. 
In the time of writing the ip of the master was 52.73.41.179

## Connection and authentication

To authenticate the client sends a message to the Master server
in the following form:

	CO#<Username>#<MD5 hashed password>#%

If the login and password are correct the server will answer with:

	VNAL#<Username>#%

Else it will answer with:

	No#%

When client connects to server server asks master server who has connected
in the following form:

	CHIP#<ip of the connected client>#<port of the connected client>#%

The server will answer the following if the client with such ip have
authenticated with master:

	OKAY#<Username>#<ip of the client when connecting to master>#<port of the client when connecting to master>#%

## Getting info about servers

To get info about existing servers the client sends to master:

	RPS#<server_index>#%

If server with such index exists -- master sends:

	SDP#<server_index>#<server_name>#<server_ip>#<server_port>#<server_description>#<link>#<??? no or empty>#%

## Getting info from server

Server:
	PC#<number of players on server>#<limit of players>#<number of characters>#<number of music tracks>#<number of areas>#<number of items>#<??? my had no>#%

## Getting privileges

To get special privileges, you need to be in mod or animator list and also authenticate sending the following:

	MOD#AUTH#<Password in plain text>#%

if all good, server will answer with:

	MODOK#%

## Getting info about animators

Client:

	GaB#<Mod ID>#%

Server:

	GaB#<Animator ID>#<Animator Username>#%

Note: When you request id of animator that doesn't exist
it just gives you info about the first mod on server.
For example:
Client:

    GaB#100500#%

Server:

    GmB#1#UnrealMod1337#%

## Getting info about mods

Client:

	GmB#<Animator ID>#%

Server:

	GmB#<Animator ID>#<Mod Username>#%

Note: When you request id of a mod that doesn't exist
it gives you:

    LCA#Username#$NO#%

Whatever it means...

## Getting info about characters

Client:

	RCD#<Char ID>#%

Server:

	CAD#<Char ID>#<Char Name>#<Taken>#<Char ID + 1>#<Char ID + 1 name>#<Taken>#%

Note: if the last char is asked it will send only info about the last char;


## Getting info about music

Client:

	RMD#<Music ID>#%

Server:

	MD#<Music Name>#<where to find music(either localhost or link to the music track)>#%

## Getting info about locations

Client:

	RAD#<Loc ID>#%

Server:

	AD#<Loc ID>#<Loc Name>#<How many chars in it>#<background name pattern>#<??? always was empty>#%

## Picking a character

To pick a character, client sends the following message:

	Req#<Character ID>#<maybe here should be something, but every packet i got had this field empty>#%

If all good the server will answer with:

	Allowed#<Character name>#%

Else it will answer with:

	TKN#%

When changing a character client firstly sends:

	Change#%

## Send IC message

Client:

	MS#<Name of the Character>#<Sprite>#<Message>#<Box name>#<Color>#<ID of the character>#<background image name>#<Position>#<Flip>#<SFX>#%

	Box name: either "char" which indicates that character name should be used or $ALT for mysteryname or your Username.
	Color: 0 - white, 1 - blue, 2 - pink, 3 - yellow, 4 - green, 5 - orange, 6 - red.
	Position: 1 - left, 2 - right, 3 - center.
	Flip: 0 - off, 1 - on.

If all good server will send this message to all clients in the same room.

Note: # and % are escaped as <pound> and <percent>.

## Send OOC message

Client:

	CT#<Username>#<Message>#%

If all good the server will send this message to ALL clients.

## Play music

Client:

	MC#<Character Name>#<Track name>#<Track ID>#<Character ID>#<looping>#%

If all good the server will send this message to ALL clients in the same room.

## Play URL music

You need to be mod to do this.

Client:

	FORCESTREAM#<Stream link>#%

Server:

	FORCESTREAM#<Stream link>#<Character Name>#%

## Show URL picture

You need to be mod to do this.

Client:

	SHOWURL#<URL>#%

Server:

	SERVURL#<URL>#%

## HP thing

We don't really use it. But it appears very often in logs so i decided to write at least anything about it.
That's just a speculation, i didn't really check.

Client:

	HP#<GOOD or EVIL>#<First bar WAS>#<First bar BECAME>#<Second bar WAS>#<Second bar BECAME>#<First bar color>#<Second bar color>#%

## Changing area

To change the area, client sends the following message:

	ARC#<Area ID>#<again my packets had it empty>#%

The server will answer with:

	ROOK#<???>#<???>#% *both fields were 30 in my packets*

And also if succesfull it will send the following command to ALL clients:

	RoC#<ID of the loc the client leaves>#<New number of chars in loc>#<ID of the loc the client joins>#<New number of people in loc>#%

## Getting list of characters in a loc

The client sends:

	LIST#<ID of the loc>#%

The server answers with _n_ messages in the form:

	POPUP#<Character name(ID)>#%

where n is the number of characters in the loc.


## Mysterious commands

### HB

Client:

	HB#%

Server:

	HB#%


My speculation is that the client have a timeout at which it checks if the connection is established.
It sends this command to the server and if it answers with the same command then all good.

### YI and MI

Server:

	YI#<int>#<int>#%

Client:

	MI#<int>#%

Couldn't really understand what they do. It seems that firstly server sends the YI#0#0#% to the client
and then client asks the server other fields. For example:

	MI#1#% -> YI#1#<int>#%
	MI#2#% -> YI#2#<int>#%

But it doesn't always answer...

### RaC

When the client picks a character, the server will send:

	RaC#<int>#<int>#%

Maybe it has something to do with how many people in area...
