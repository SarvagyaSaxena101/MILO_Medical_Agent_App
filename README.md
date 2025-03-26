
# Medical Diagnosis Assistant App

A comprehensive Android application that leverages AI and location services to provide medical diagnosis assistance and connect users with healthcare professionals.

## Overview

The Medical Diagnosis Assistant App is a sophisticated mobile application that combines artificial intelligence, image processing, and location-based services to provide users with medical analysis capabilities and healthcare provider discovery.

## Features

### 1. Medical Image Analysis
- **Bone Fracture Detection**: Upload X-ray images for fracture analysis
- **MRI Scan Analysis**: Process and analyze MRI scans
- **Diabetes Detection**: Analyze relevant medical images for diabetes indicators
- **Pneumonia Detection**: Chest X-ray analysis for pneumonia detection
- **Alzheimer's/Dementia Detection**: Brain scan analysis for early detection

### 2. Location-Based Services
- Find nearby medical specialists
- Access detailed doctor profiles and specializations
- Get immediate contact information
- Real-time location tracking for accurate results

### 3. AI Medical Assistant
- Interactive medical consultation
- Voice command support for hands-free operation
- Natural language processing for medical queries
- Real-time response system

## Technical Specifications

### Architecture
- **Pattern**: MVVM (Model-View-ViewModel)
- **Language**: Kotlin
- **Minimum SDK**: 24
- **Target SDK**: 35
- **Build System**: Gradle with KTS

### Dependencies
```gradle
// Core Dependencies
- AndroidX Core KTX
- AndroidX AppCompat
- Material Design Components
- AndroidX Activity
- AndroidX ConstraintLayout

// Networking
- Retrofit2
- OkHttp3
- Volley

// Authentication
- Firebase Auth

// Location Services
- Google Play Services Location

// Data Processing
- Gson
```

### API Endpoints
```
POST /predict/bone_fracture
POST /predict/mri_scan
POST /predict/diabetes
POST /predict/pneumonia
POST /predict/dementia
GET  /therapist
GET  /getdoctors
```

## Setup and Installation

1. Clone the project
2. Open in Android Studio or use Replit's Android development environment
3. Sync Gradle files
4. Configure API keys in the environment settings
5. Build and run the application

## Build Configuration

The project uses a custom Gradle build workflow:
```bash
./gradlew clean    # Clean build files
./gradlew build    # Build the project
./gradlew installDebug  # Install debug version
```

## Testing

The project includes both unit tests and instrumented tests:
- **Unit Tests**: Located in `app/src/test/`
- **Instrumented Tests**: Located in `app/src/androidTest/`

## Security Features

- Secure API communication using HTTPS
- OAuth2 authentication
- Location permission handling
- Secure data storage
- API key protection

## Project Structure

```
app/
├── src/
│   ├── main/
│   │   ├── java/com/example/myapplication/
│   │   │   ├── activities/
│   │   │   ├── services/
│   │   │   └── models/
│   │   └── res/
│   ├── test/
│   └── androidTest/
```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Support

For support and queries, please create an issue in the project repository.

---

**Note**: This application is intended for assistance purposes only and should not be used as a replacement for professional medical advice.
