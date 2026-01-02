# Customer App – Booking Agent

Customer App for the travel destination booking.
The app allows users to log in using email OTP, browse available destinations, and send booking requests.

---

## Features

- **Email OTP Login**
  - Users authenticate using email-based OTP.
    <div align="center">
  <img src="images/SignIn.jpg" alt="Sign In" width="20%" />
  <img src="images/Verification.jpg" alt="Verification" width="20%" />
</div>

- **Browse Destinations**
  - Horizontally scrollable destination cards.
  <img src="images/Home.jpg" alt="Teachers Screen" width="20%"/>
- **Destination Details**
  - View detailed information about a selected destination.
  <img src="images/Details.jpg" alt="Teachers Screen" width="20%"/>
- **Book Now**
  - Users can send a booking request for a destination.
  <img src="images/Booking_Requesting.jpg" alt="Teachers Screen" width="20%"/>
- **Real-time Status Updates**
  - Booking status (`Requesting / Accepted / Rejected`) updates instantly without full screen refresh.
  <img src="images/Booking_Accepted.jpg" alt="Teachers Screen" width="20%"/>

---

## Real-time Sync

- Firebase Firestore is used for real-time synchronization.
- Booking status updates from the Owner App are reflected instantly in the Customer App.

---

## Tech Stack

- **Language:** Kotlin
- **UI:** Jetpack Compose
- **Architecture:** MVVM
- **State Management:** StateFlow
- **Backend:** Firebase Firestore
- **Authentication:** Email OTP (Descope Email OTP Auth)
- **Async:** Kotlin Coroutines & Flow

---
## Owner App

- **Owner App Repository:**  
  https://github.com/001iftikar/Owner-Intern.git

