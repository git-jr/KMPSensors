
import SwiftUI
import shared
import CoreMotion
import Combine



struct ComposeView: UIViewControllerRepresentable {

    func makeUIViewController(context: Context) -> UIViewController {
        let mainViewController = Main_iosKt.MainViewController()

        let motionManager = MotionManager()
        motionManager.startUpdates()
        return mainViewController
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct ContentView: View {
    var body: some View {
        ComposeView()
                .ignoresSafeArea(.all, edges: .bottom) // Compose has own keyboard handler
    }
}



class MotionManager: ObservableObject {
    private let motionManager = CMMotionManager()
    private var cancellables: Set<AnyCancellable> = []

    @Published var motionData: (acceleration: CMAcceleration, rotationRate: CMRotationRate) = (.init(x: 0, y: 0, z: 0), .init(x: 0, y: 0, z: 0))

    @Published var rollAngle: Double = 0.0
    @Published var pitchAngle: Double = 0.0

    init() {
        motionPublisher
            .receive(on: DispatchQueue.main)
            .assign(to: \.motionData, on: self)
            .store(in: &cancellables)
    }

    func startUpdates() {
        motionManager.startAccelerometerUpdates()
        motionManager.startGyroUpdates()

        Timer.publish(every: 0.1, on: .main, in: .default)
            .autoconnect()
            .sink { [weak self] _ in
                guard let self = self else { return }

            if let accelerometerData = self.motionManager.accelerometerData {
                let acceleration = accelerometerData.acceleration
                if let gyroData = self.motionManager.gyroData {
                    let rotationRate = gyroData.rotationRate
                    let motionData = (acceleration: acceleration, rotationRate: rotationRate)
                    self.motionPublisher.send(motionData)

                    let rollAngle = atan2(acceleration.x, acceleration.z)
                    self.rollAngle = rollAngle * 180 / .pi // Converter to degrees
                    // print("rollAngle: \(self.rollAngle)")

                    if self.rollAngle > 20.0 {
                        print("Celular foi virado para a direita")
                    } else if self.rollAngle < -20.0 {
                        print("Celular foi virado para a esquerda")
                    }

                    let pitchAngle = atan2(acceleration.y, acceleration.z)
                    self.pitchAngle = pitchAngle * 180 / .pi  // Converter to degrees
                    // print("pitchAngle: \(self.pitchAngle)")
                    }
                }
            }
            .store(in: &cancellables)
    }

    func stopUpdates() {
        motionManager.stopAccelerometerUpdates()
        motionManager.stopGyroUpdates()
        cancellables.forEach { $0.cancel() }
    }

    var motionPublisher: PassthroughSubject<(acceleration: CMAcceleration, rotationRate: CMRotationRate), Never> = PassthroughSubject()
}