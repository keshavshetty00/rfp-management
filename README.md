rfp-management
This project is an AI‑powered RFP management system that streamlines procurement by generating structured RFPs, managing vendors, sending requests, and comparing proposals. It was built  to demonstrate end‑to‑end automation of the RFP workflow.



 Tech Stack
- Backend: Java, Spring Boot, MySQL
- Frontend: HTML, CSS, JavaScript
- APIs: REST endpoints for RFP, vendor, email, and proposal comparison

 How to Run
1. Backend
   - Configure MySQL database in `application.properties`.
   - Run Spring Boot app:
     ```bash
     mvn spring-boot:run
     ```
   - Backend runs at `http://localhost:8080`.

2. Frontend
   - Place `index.html` in `src/main/resources/static/`.
   - Open `http://localhost:8080/index.html` in your browser.

 Example Flow
1. Create an RFP from description.  
2. Map vendors to the RFP.  
3. Send RFP to vendors.  
4. Submit inbound proposals.  
5. Compare proposals and view results.

 Notes
- Large files (PDFs, logs, zips) are excluded via `.gitignore`.  
- For assignment review, only source code and lightweight assets are included.  
- External documents can be linked separately if needed.


© 2025 AI-Powered RFP Management System | Built for SDE Assignment
