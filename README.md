# Event Management Application

## by Colby Sparks

The program I am building is an event management app, so essentially a to-do list with a few bells 
and whistles.  I used to do sound for a music/arts venue downtown and my friend Jo, the person who managed the space, 
was always talking about how terrible his booking system was.  He was licensing it from some small software developer,
but it was lacking basic features that would have made his job a lot easier.  

Some of the basic features he wanted were:
- to view **all** scheduled events in a scheduler
- to be able to **edit events** from the scheduler window
- the capacity to **store riders and stageplots** with info on sound specifications and stage layout for each band

People who manage venues and book shows are very few, so on top of the above functionality I would also like
to include simple features (which happen to be a subset of the features of an even-management app) so that users
can:
- **create simple events** with date/time/location and any additional information
- **view/select/edit** their stored events on a calendar
- view events according to **importance**

This would be an application that your average person could use, but also something that would hopefully be useful to
someone like Jo, who needs to keep track of many events with very detailed information.

This project is of personal interest to me because I used to work in the music industry, and I would like to actually
create a program that my friend might find useful.  I have bigger plans for more features that I'd like to implement,
but I don't want to get ahead of myself.  For now, I think the features described above will be useful enough on
their own.

## User Stories

#### Simple User:
- I want to keep track of a number of different upcoming events with dates, times and locations
- I want to set a different level of importance for each event
- I want to view the event alongside any other events that I have created
- If I've made any new changes to my events, I want the program to ask me if I want to save before quitting

#### User (concert promoter/venue manager):
- I want to schedule a concert with a date, start time and location
- I want to store information about the different acts, presale tickets (number and price) and who will be working
bar/sound/lighting
- I want to enter any information regarding expenses--how much bands and workers need to be paid, the up-front cost on
alcohol, cost of any rentals needed for the show, etc.
- I want the software to tally expenses and compute projected revenue for a concert
- I want to edit events: change dates or any other information stored in an event
- I want to be able to save my shows so I can come back and edit them at a later time


Phase 4: Task 2
EventDate class is now robust.  Any method in this class involved with setting or modifying a date will now
throw DateFormatException if any of the new date values are invalid (if user tries to set the month value to 13 
or day to 0 etc..).  This is achieved using the method verifyDateValues in the EventDate class.