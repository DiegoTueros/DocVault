# DocVault - Gestión de Documentos Confidenciales

## Descripción
DocVault es una aplicación para Android que permite almacenar y visualizar documentos confidenciales con protección biométrica, cifrado AES-256 y control de acceso.

---

## Características principales
- Pantalla de documentos con listado y filtros por tipo (PDF, Imagen)
- Agregar documentos desde galería o cámara
- Cifrado seguro de documentos
- Visualización de imágenes con zoom (pinch-to-zoom)
- Visualización de PDF con PdfRenderer
- Autenticación biométrica antes de abrir documentos
- Prevención de screenshots (seguridad)
- Marca de agua con ubicación (calle) sobre imágenes
- Registro de accesos por documento (historial con fecha/hora)

---

## Arquitectura
- Patrón: MVI (Model-View-Interactor)
- Composición de UI: Jetpack Compose
- Navegación: Navigation Compose
- Gestión de estado: StateFlow + SharedFlow
- Dependencias principales:
    - Jetpack Compose Material3
    - AndroidX Biometric
    - Google Play Services Location

---

## Seguridad
- Documentos almacenados en ruta cifrada con AES-256
- Acceso controlado vía autenticación biométrica
- Registro de accesos para auditoría
- Marca de agua con geolocalización para rastreo

---

## Estructura de proyecto
com.docvault
├── 📁 feature_docs
│ ├── 📁 documents
│ │ ├── 📄 DocumentsScreen.kt
│ │ ├── 📄 DocumentsViewModel.kt
│ │ └── 📁 interactor
│ ├── 📁 documents_viewer
│ │ └── 📄 DocumentViewerScreen.kt
│ └── 📁 components
├── 📁 domain
│ └── 📁 model / usecase / repository
├── 📁 data
│ └── 📁 local / mapper / repository / security
└── 📁 navigation

---

## Decisiones de diseño
- Diferenciación de PDF vs Imagen: `MIME type` desde content resolver
- Almacenamiento: `SecureFileStorage` cifrado
- Visualización: PdfRenderer para PDFs y Image con pinch-to-zoom para imágenes
- Biometría centralizada para documentos confidenciales
- Prevención de screenshot implementada para la pantalla de visualización
- Historial de accesos y marca de agua basada en ubicación

---