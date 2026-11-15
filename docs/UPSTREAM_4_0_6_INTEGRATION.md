# Intégration des mises à jour officielles OpenWispr 4.0.6

- Référence du projet officiel : EdiBianco/OpenWhispr `4e127a7a61958ba44cf7ea0163c1748507b6df66`.
- Référence personnalisée : WuCorp-France/OpenWhispr `d256706036d07b6eb9e45c506e2d004e3c275671`.
- Intégration des 21 commits officiels, y compris le sélecteur de modèles Groq, les modèles locaux et corrections, les commandes vocales utilisant le fournisseur configuré, l'interface de téléchargement/mise à jour et la prévisualisation de taille.
- Les statistiques locales, le mode de rédaction soutenu et le correctif du bouton flottant sur l'écran verrouillé/AOD sont conservés.
- Résolution spécifique : `CleanupProviderConfig.kt` conserve le paramètre `styled` et adopte `chatRequest` pour les commandes vocales ; `WhisperAccessibilityService.kt` conserve la protection des sessions et l'AOD, ajoute le modèle de transcription choisi, le fournisseur de commandes et adapte la prévisualisation au widget à deux boutons ; `app/build.gradle.kts` conserve l'identifiant WuCorp et installe cet APK de test séparément.
- Les modifications d'interface (`MainActivity.kt`), de téléchargement (`UpdateChecker.kt`) et de chaînes de traduction ont été intégrées en conservant les personnalisations.
- Une fois la compilation et les tests unitaires réussis, tester sur Android réel : Groq, mode soutenu, statistiques, AOD/verrouillage, sélecteur de modèle et prévisualisation du widget.
- La branche `main` du fork reste inchangée. Ce travail est sur une branche d'intégration distincte.
