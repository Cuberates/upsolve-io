# Upsolve: Experimental Knowledge Management System for Competitive Programming. 

## 1. Introduction
- [Competitive Programmers](https://en.wikipedia.org/wiki/Competitive_programming) have different systems for intensive studying before programming competitions, but there has never been a personalized system, which also happens to be minimalistic, specifically targeting this stage of preparation. Most programmers solve many problems on drastically different platforms but have no system for managing, reviewing, or learning from them after participating in contests.

### 1.1 Spaced Repetition + Active Recall
- Top students in any field have some knowledge management system to keep track of their consumption of new topics. Two of the most well-studied learning methods that have been shown to enhance knowledge retention are "Spaced Repetition" and "Active Recall," supported by powerful management systems such as flashcards, jotted notes, and dynamic note-taking tools (Anki, Notion, Obsidian, etc.).
- When used correctly in combination, not only are students able to memorize a large quantity of concepts before exams, but also be able to retain these concepts long-term for future use.

### 1.2. Application in Competitive Programming
- In Competitive Programming, it is crucial for contestants to have complex algorithms at hand to solve a wide range of programming problems, and to have experience implementing and modifying them to satisfy problem constraints. Unlike the typical software development process, competitive programming is fast-paced. Programming competitions like the [International Collegiate Programming Contest](https://icpc.global/) require contestants to implement many data structures and algorithms in under an hour, solving dozens of problems within hours or less. Essentially, we want a minimalist system much like Anki, but with features tailored to memorizing competitive programming problems.
- *However, the features of this application can also be used for storing knowledge in just about any discipline. Whether it is for studying the features of, say, CLI tools, or memorizing the low-level features of C++20, the same study methods can be applied.*

## 2. Features of Algobase
### 2.1. [F1] Creative Mode
- Users can **create cards**, each containing:
  - A **problem statement** (description of a coding challenge) written in Markdown. 
  - A **designated code editor** with syntax highlighting for implementation
- Each card is assigned a **difficulty level** (e.g., *Easy*, *Medium*, *Hard*)

### 2.2. [F2] Study Mode
- Users can **study** a collection of cards.
- The platform:
  - Displays a **stored problem statement** with an **empty code editor**.
  - Prioritizes cards by their **difficulty level** (e.g., starting from *Easy* or *Hard* based on user preference)
  - Allows the user to **input their code solution**.
  - Compares the user’s implementation against the **stored reference implementation**.
  - **Adjusts the difficulty level** dynamically based on user performance and accuracy.
 
## 3. Team Structure
#### Team Lead & Merger: [@Cuberates](https://github.com/Cuberates) 

#### Backend Team: [@Cuberates](https://github.com/Cuberates), [@gurneetgill](https://github.com/gurneetgill).
- Handle all pull requests and performing all repository merging.
- Integrate all database operations and handle essential server-side features.
- Managing codebase for the application, and implement core functionalities of all features.  

#### Frontend Team: [@Hayman217](https://github.com/hayman217), [@IsrakFarhanSFU](https://github.com/IsrakFarhanSFU), [@yuvrajchahal22](https://github.com/yuvrajchahal22).
- Research and design responsive UI/UX components and prototypes. 
- Implement UI/UX layouts for all features of the application.
