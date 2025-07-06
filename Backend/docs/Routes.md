# Routes for the website
<hr>

- {{ip}}: localhost:8080
- {{api_version}}: /api/v1

### /home => Frontend
- HeroItemModel.ts
    - GET: {{ip}}/{{api_version}}/public-dashboard/hero-items => id, title, description, imageUrl
    - PUT: {{ip}}/{{api_version}}/public-dashboard/hero-items => title, description, imageUrl

- AnnouncementsModel.ts
    - GET: {{ip}}/{{api_version}}/announcements => id, title, message, type, startDate, endDate, createdBy
    - POST: {{ip}}/{{api_version}}/announcements => title, message, type, startDate, endDate, createdBy
