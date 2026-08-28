🟦 PHASE 1 — FRONTEND FOUNDATION
TALENTBRIDGE — PHASE 1: FRONTEND FOUNDATION

You are my senior React frontend engineer.

We are improving an existing project called TalentBridge, a Campus Recruitment Platform connecting:

- Students
- Recruiters
- Administrators

CURRENT STACK:

Frontend:
- React
- Tailwind CSS
- Axios
- React Router

Backend:
- Java
- Spring Boot
- Spring Security
- JWT
- JPA/Hibernate
- Maven

Database:
- PostgreSQL

IMPORTANT:
The backend already exists and must NOT be rebuilt.

Do NOT:
- Replace Spring Boot
- Replace PostgreSQL
- Replace React
- Change working API endpoints
- Change authentication architecture
- Remove existing functionality
- Introduce unnecessary libraries
- Rewrite working code without a reason

The goal of this phase is to create a clean, reusable and professional frontend foundation.

==================================================
CURRENT APPLICATION
==================================================

The application currently has:

STUDENT:
- Dashboard
- Profile
- Account Details
- Academic & Placement Profile
- Technical Skills
- Resume URL
- Available Jobs
- My Applications
- Job Search
- Job Application

RECRUITER:
- Dashboard
- Profile
- Company & Hiring Information
- Create Job Posting
- Job Postings
- Applicants

ADMIN:
- Master Dashboard
- Admin Profile
- Registered Users
- Active Postings
- Submitted Applications
- User Directory
- Role Assignment
- User Management

The current UI is functional but needs better architecture, consistency and professional UI/UX.

==================================================
PHASE 1 OBJECTIVES
==================================================

We will complete the following tasks IN ORDER:

TASK 1 — Existing Frontend Audit

Inspect the entire frontend.

Identify:
- Folder structure
- Pages
- Components
- Routes
- API/service files
- Authentication logic
- JWT handling
- Role-based routing
- Tailwind configuration
- Existing reusable components
- Duplicate code
- Hardcoded/mock data
- Existing loading states
- Existing error handling
- Existing notifications
- Existing responsive implementation

DO NOT MODIFY ANY FILE FOR TASK 1.

Give me a clear report and STOP.

==================================================

TASK 2 — Frontend Architecture

After I say "Proceed with Task 2":

Improve the frontend structure only where necessary.

Create a maintainable structure for:
- Pages
- Components
- Layouts
- Services/API
- Hooks
- Utilities
- Authentication
- Role-based functionality

Reuse existing files whenever possible.

Do not create duplicate functionality.

STOP after Task 2.

==================================================

TASK 3 — Design System

After I say "Proceed with Task 3":

Create a consistent visual system using the existing Tailwind setup.

Standardize:
- Colors
- Typography
- Spacing
- Border radius
- Shadows
- Buttons
- Inputs
- Selects
- Textareas
- Cards
- Badges
- Tables
- Modals
- Alerts
- Form states

Keep the current TalentBridge visual identity:
- Professional
- Modern
- Clean
- Recruitment/SaaS style
- Blue/purple primary visual language

Do not make it overly flashy.

STOP after Task 3.

==================================================

TASK 4 — REUSABLE UI COMPONENTS

After I say "Proceed with Task 4":

Create or improve reusable components for:

- Button
- Input
- Select
- Textarea
- Card
- Badge
- Modal
- Confirmation Dialog
- Toast/Notification
- Loading Spinner
- Loading Skeleton
- Empty State
- Error State
- Page Header
- Section Header

Before creating a component, check whether an equivalent already exists.

Do not duplicate components.

STOP after Task 4.

==================================================

TASK 5 — GLOBAL LAYOUT

After I say "Proceed with Task 5":

Improve global application layouts.

Create/improve:

- Student layout
- Recruiter layout
- Admin layout
- Navbar/Header
- Sidebar where appropriate
- Page container
- Navigation states
- Active route styling
- Logout UI
- Profile navigation

Ensure the correct layout appears according to the user's role.

Do not break existing routing or authentication.

STOP after Task 5.

==================================================

TASK 6 — GLOBAL STATES

After I say "Proceed with Task 6":

Improve global:

- Loading states
- Skeleton states
- Empty states
- Error states
- Success states
- Toast notifications
- API failure handling
- Form submission states

Do not hide backend/API errors.

Errors should be understandable to the user.

STOP after Task 6.

==================================================
WORKING RULES
==================================================

ONE TASK AT A TIME.

Before modifying files:
1. Inspect existing implementation.
2. Identify affected files.
3. Explain the intended change.
4. Reuse existing code where possible.

After modifying:
1. List modified files.
2. List created files.
3. Explain changes.
4. Explain how to test.
5. Mention any backend dependency.
6. Mention any issue found.

Then STOP.

Never automatically proceed to another task.

START WITH TASK 1 ONLY.
🟩 PHASE 2 — STUDENT EXPERIENCE
TALENTBRIDGE — PHASE 2: STUDENT EXPERIENCE

Phase 1 frontend foundation has been completed.

Now improve the STUDENT side of TalentBridge.

IMPORTANT:
Do not rebuild the backend.

Do not change:
- Spring Boot
- PostgreSQL
- Existing API contracts
- JWT authentication
- Existing backend functionality

Use the existing APIs and frontend services.

==================================================
STUDENT FEATURES CURRENTLY AVAILABLE
==================================================

The Student currently has:

- Student Dashboard
- Student Profile
- Account Details
- Academic & Placement Profile
- Technical Skills
- Resume URL
- Available Jobs
- My Applications
- Job Search
- Job Application

==================================================
GOAL
==================================================

Transform the student experience from a basic CRUD interface into a professional recruitment platform.

==================================================
TASK ORDER
==================================================

TASK 1 — Student Dashboard

Improve:
- Welcome section
- Profile completion
- Quick statistics
- Available jobs summary
- Application summary
- Recent applications
- Recommended actions
- Empty states
- Loading states

Use real API data.

Do not invent data if API data exists.

STOP.

==================================================

TASK 2 — Student Profile

Improve:
- Account information
- Academic information
- Placement information
- Technical skills
- Resume section

Make the profile visually organized.

STOP.

==================================================

TASK 3 — Profile Completion

Create a professional profile completion indicator.

Calculate completion based on meaningful profile fields.

Show:
- Percentage
- Missing information
- Clear action to complete profile

Do not use fake completion values.

STOP.

==================================================

TASK 4 — Skills UI

Improve technical skills.

Display skills as:
- Tags/chips
- Clean spacing
- Add/remove/edit interaction if existing API supports it

Preserve the existing backend structure.

STOP.

==================================================

TASK 5 — Resume Section

Improve resume presentation.

Include:
- Resume URL
- View/open action
- Edit action where supported
- Validation for URL
- Empty state

Do not invent file-upload functionality unless backend support already exists.

STOP.

==================================================

TASK 6 — Available Jobs

Redesign the Available Jobs interface.

Create professional job cards containing appropriate information such as:
- Job title
- Company
- Location
- Salary
- Job type
- Posted information
- Apply button

Use real backend data.

STOP.

==================================================

TASK 7 — Search

Improve job search.

Search by:
- Role/title
- Company
- Location

Use existing API capabilities if available.

Avoid unnecessary API requests while typing.

STOP.

==================================================

TASK 8 — Filters & Sorting

Add useful job filters where supported:

- Job type
- Location
- Salary
- Other relevant fields

Add sorting where appropriate.

Do not create frontend filters for data that cannot be reliably obtained.

STOP.

==================================================

TASK 9 — Job Details

Create a professional Job Details view.

Display:
- Job title
- Company
- Location
- Salary
- Job type
- Description
- Requirements
- Application action

Use a page or modal depending on the existing routing architecture.

STOP.

==================================================

TASK 10 — Apply Job

Improve the application flow.

Include:
- Clear Apply button
- Confirmation
- Loading state
- Success state
- Error state
- Already-applied state

Do not allow accidental duplicate applications if backend already prevents them.

STOP.

==================================================

TASK 11 — My Applications

Create a professional application tracking interface.

Show:
- Job
- Company
- Applied date
- Current status
- Relevant actions

Use visual status badges.

If appropriate, show a status progression such as:

Applied → Under Review → Shortlisted → Interview → Selected/Rejected

Only use statuses supported by the backend.

STOP.

==================================================

TASK 12 — STUDENT RESPONSIVENESS

Make all Student pages work properly on:

- Desktop
- Laptop
- Tablet
- Mobile

Do not simply shrink the desktop UI.

Rearrange layouts intelligently.

STOP.

==================================================
RULES
==================================================

ONE TASK AT A TIME.

Before changing anything:
- Inspect existing implementation.
- Identify relevant files.
- Check existing API calls.
- Reuse existing components.

Never:
- Replace backend
- Invent APIs
- Use fake data
- Remove existing functionality
- Break authentication
- Duplicate components
- Automatically continue to the next task

After each task:
- List modified files
- List created files
- Explain changes
- Explain testing
- Mention API dependencies/issues

Then STOP.

START WITH TASK 1 ONLY.
🟪 PHASE 3 — RECRUITER EXPERIENCE
TALENTBRIDGE — PHASE 3: RECRUITER EXPERIENCE

Now improve the RECRUITER side of TalentBridge.

The backend already exists.

Do NOT rebuild or replace:
- Spring Boot
- PostgreSQL
- JWT
- Existing APIs
- Authentication
- Existing backend functionality

Use the existing frontend architecture and reusable components created in previous phases.

==================================================
CURRENT RECRUITER FEATURES
==================================================

Currently available:

- Recruiter Dashboard
- Recruiter Profile
- Company & Hiring Information
- Create Job Posting
- Job Postings
- Applicants

==================================================
GOAL
==================================================

Transform the recruiter experience into a professional hiring management platform.

==================================================
TASK ORDER
==================================================

TASK 1 — Recruiter Dashboard

Improve the dashboard.

Include useful statistics such as:
- Total job postings
- Active postings
- Total applicants
- Applications requiring attention

Use real API data.

Do not invent statistics.

STOP.

==================================================

TASK 2 — Job Posting Management

Redesign the job posting list.

Show:
- Job title
- Company
- Location
- Salary
- Job type
- Status
- Applicant count
- Actions

Include professional empty/loading/error states.

STOP.

==================================================

TASK 3 — Create Job Posting

Improve the Create Job Posting form.

Current fields include:
- Job Title
- Company
- Location
- Salary
- Job Type
- Description

Improve:
- Form layout
- Validation
- Error messages
- Required fields
- Salary validation
- Character limits where appropriate
- Submission loading
- Success feedback

Do not modify the backend contract unless absolutely required.

STOP.

==================================================

TASK 4 — Edit Job

If the existing backend supports editing:

Create a professional Edit Job experience.

Reuse the Create Job form where possible.

Avoid duplicate forms.

STOP.

==================================================

TASK 5 — Job Actions

Improve job management actions.

Where backend functionality exists, support:
- Edit
- Close/deactivate
- Delete

Use confirmation dialogs for destructive actions.

Never perform destructive actions without confirmation.

STOP.

==================================================

TASK 6 — Applicants

Redesign the Applicants section.

Show:
- Applicant name
- Email
- Job applied for
- Application date
- Application status

Use a professional table/card layout depending on screen size.

STOP.

==================================================

TASK 7 — Applicant Details

Create a detailed applicant view.

Show available:
- Student profile information
- Academic information
- Technical skills
- Resume
- Application information
- Status

Do not expose data that the backend does not provide.

STOP.

==================================================

TASK 8 — Application Status

Improve recruiter application management.

Allow recruiters to change application status if the existing API supports it.

Use:
- Clear status dropdown
- Confirmation where appropriate
- Loading state
- Success notification
- Error handling

STOP.

==================================================

TASK 9 — Recruiter Profile

Improve:
- Personal information
- Company information
- Designation
- Company website
- Company description

Add appropriate validation.

STOP.

==================================================

TASK 10 — RESPONSIVE RECRUITER UI

Ensure recruiter pages work correctly on:

- Desktop
- Laptop
- Tablet
- Mobile

Tables should transform intelligently on smaller screens.

STOP.

==================================================
RULES
==================================================

ONE TASK AT A TIME.

Inspect before modifying.

Reuse components from Phase 1.

Reuse API services.

Never:
- Invent backend APIs
- Use fake applicant data
- Break authentication
- Replace backend
- Duplicate forms/components
- Remove existing functionality

After each task:
1. Modified files
2. Created files
3. What changed
4. How to test
5. API dependencies
6. Issues discovered

Then STOP.

START WITH TASK 1 ONLY.
🟥 PHASE 4 — ADMIN EXPERIENCE
TALENTBRIDGE — PHASE 4: ADMIN EXPERIENCE

Now improve the ADMIN side of TalentBridge.

The Admin has elevated privileges.

The backend already exists.

Do NOT rebuild or replace the backend.

Do NOT change:
- Spring Boot
- PostgreSQL
- JWT
- Existing API contracts
- Existing authentication
- Existing role system

==================================================
CURRENT ADMIN FEATURES
==================================================

Current Admin functionality includes:

- Master Dashboard
- Registered Users
- Active Postings
- Submitted Applications
- User Directory
- Role Assignment
- User Management
- Admin Profile

==================================================
GOAL
==================================================

Create a professional administration console for managing the TalentBridge platform.

==================================================
TASK ORDER
==================================================

TASK 1 — Master Dashboard

Improve:
- Total registered users
- Active job postings
- Submitted applications
- Other meaningful statistics supported by the backend

Use real data.

Do not fabricate numbers.

Add:
- Loading states
- Error states
- Empty states

STOP.

==================================================

TASK 2 — User Directory

Improve the user management table.

Show:
- User ID
- Name
- Email
- Role
- Account status if supported

Make it:
- Clean
- Searchable if appropriate
- Responsive
- Easy to scan

STOP.

==================================================

TASK 3 — Role Management

Improve role assignment UI.

Roles currently include:
- ADMIN
- STUDENT
- RECRUITER

Use a clear role selector.

Before changing a role:
- Confirm if necessary
- Show loading state
- Show success/error notification

Do not allow unsafe role changes that could compromise the master admin account.

STOP.

==================================================

TASK 4 — User Deletion

Improve user deletion.

Use:
- Confirmation modal
- Clear warning
- Loading state
- Success notification
- Error handling

Do not silently delete users.

STOP.

==================================================

TASK 5 — Admin Activity / Overview

If existing backend data supports it, improve the dashboard with useful administrative information.

Do not create fake activity logs.

If backend support is missing, tell me instead of inventing data.

STOP.

==================================================

TASK 6 — Admin Profile

Improve:
- Admin identity
- Account information
- Security/elevated privilege indication
- Profile fields

Keep the master admin protected.

STOP.

==================================================

TASK 7 — ADMIN RESPONSIVENESS

Make the entire admin console responsive.

Desktop:
- Full tables
- Dashboard cards

Tablet:
- Adapted layouts

Mobile:
- Card/list transformations
- Accessible controls
- No horizontal overflow

STOP.

==================================================
RULES
==================================================

ONE TASK AT A TIME.

Inspect existing code before modifying.

Reuse Phase 1 components.

Use real API data.

Never:
- Fake statistics
- Fake users
- Invent APIs
- Remove security
- Break role permissions
- Delete without confirmation
- Modify backend without explicit approval

After every task provide:
- Modified files
- Created files
- Explanation
- Testing instructions
- API dependencies
- Problems found

Then STOP.

START WITH TASK 1 ONLY.
🟨 PHASE 5 — AUTHENTICATION + SECURITY UX
TALENTBRIDGE — PHASE 5: AUTHENTICATION & SECURITY UX

Now improve the authentication and authorization experience of TalentBridge.

IMPORTANT:

The backend authentication system already exists using:

- Spring Security
- JWT
- Role-based authorization

DO NOT rebuild authentication.

DO NOT replace JWT.

DO NOT modify backend security unless I explicitly ask you to.

Your job is to improve the FRONTEND authentication UX while preserving the existing security architecture.

==================================================
CURRENT ROLES
==================================================

TalentBridge has:

- STUDENT
- RECRUITER
- ADMIN

The frontend must respect role-based access.

==================================================
TASK ORDER
==================================================

TASK 1 — Login UI

Improve the login page.

Include:
- Professional TalentBridge branding
- Email/username field according to existing backend
- Password field
- Show/hide password
- Validation
- Loading state
- Clear API errors
- Success/login transition

Do not change the backend login request structure.

STOP.

==================================================

TASK 2 — Registration UI

Improve registration.

Include:
- Clean form
- Validation
- Password requirements if supported
- Confirm password where appropriate
- Loading state
- Error handling
- Success feedback

Preserve existing API contract.

STOP.

==================================================

TASK 3 — Authentication State

Inspect and improve frontend authentication state management.

Ensure:
- Logged-in state persists correctly
- JWT is handled correctly
- User information is available where required
- Logout clears authentication state correctly

Do not expose tokens unnecessarily.

STOP.

==================================================

TASK 4 — Protected Routes

Improve protected route handling.

Users should not access protected pages without authentication.

Handle:
- Unauthenticated users
- Loading authentication state
- Invalid/expired sessions

Do not rely only on frontend protection for actual security.

The backend remains the source of authorization.

STOP.

==================================================

TASK 5 — Role-Based Routing

Ensure:

STUDENT → Student pages

RECRUITER → Recruiter pages

ADMIN → Admin pages

Prevent inappropriate frontend navigation.

Examples:

Student should not see Admin dashboard.

Recruiter should not see Admin console.

Admin should not accidentally be treated as Student.

Backend authorization must remain authoritative.

STOP.

==================================================

TASK 6 — Unauthorized Page

Create a professional 403/Unauthorized page.

Include:
- Clear message
- Appropriate icon
- Back button
- Dashboard button where appropriate

STOP.

==================================================

TASK 7 — Session Expiration

Improve handling when:
- JWT expires
- API returns 401
- Authentication becomes invalid

Provide:
- Clear message
- Safe logout/cleanup
- Redirect to login

Do not create infinite redirect loops.

STOP.

==================================================

TASK 8 — Logout

Improve logout UX.

Ensure:
- Authentication state is cleared
- Relevant storage is cleared
- User is redirected appropriately
- No protected data remains visible

STOP.

==================================================

TASK 9 — Authentication Error Handling

Create consistent messages for:

- Invalid credentials
- Validation errors
- Unauthorized
- Forbidden
- Server unavailable
- Network error
- Session expired

Do not expose technical stack traces to users.

STOP.

==================================================

TASK 10 — FINAL AUTHENTICATION REVIEW

Audit the complete frontend authentication flow.

Check:

Login
↓
JWT/session
↓
Role detection
↓
Protected routes
↓
Role-specific dashboard
↓
API authorization
↓
Logout

Identify:
- Security risks
- UX problems
- Duplicate logic
- Broken redirects
- Race conditions
- Token handling problems

Do not make risky security changes without explaining them.

STOP.

==================================================
STRICT RULES
==================================================

ONE TASK AT A TIME.

Never:
- Replace JWT
- Replace Spring Security
- Invent authentication APIs
- Store sensitive credentials unnecessarily
- Disable authorization
- Hardcode roles insecurely
- Remove protected routes
- Bypass backend authorization
- Automatically proceed to the next task

Before changing code:
1. Inspect existing authentication implementation.
2. Identify relevant files.
3. Explain what will change.
4. Preserve existing backend contracts.

After each task:
1. Modified files
2. Created files
3. What changed
4. How to test
5. Security considerations
6. Any backend dependency
7. Any issues discovered

Then STOP.

START WITH TASK 1 ONLY.
