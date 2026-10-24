{
  description = "A very basic flake";

  inputs = {
    nixpkgs.url = "https://channels.nixos.org/nixpkgs-unstable/nixexprs.tar.zst";
    flake-utils.url = "github:numtide/flake-utils";
    mvn2nix.url = "github:fzakaria/mvn2nix";
  };

  outputs = { self, nixpkgs, flake-utils, mvn2nix }: flake-utils.lib.eachDefaultSystem (system:
    let
      pkgs = import nixpkgs {
        inherit system;
        overlays = [ mvn2nix.overlay ];
      };
      mavenRepository = pkgs.buildMavenRepositoryFromLockFile {
        file = ./mvn2nix-lock.json;
      };
    in {
      packages.default = pkgs.stdenv.mkDerivation {
        pname = "torneo-de-los-inmortales";
        version = "1.0";
        src = ./.;

        nativeBuildInputs = [ pkgs.maven pkgs.makeWrapper ];

        buildPhase = ''
          runHook preBuild
          # Copy the repo into a writable dir: store paths are read-only (Maven
          # wants to write .locks files) and Maven Resolver 2.x rejects symlinked
          # artifacts whose real path doesn't match the repo layout, so the
          # linkFarm symlinks must be dereferenced.
          cp -rL ${mavenRepository} $TMP/m2repo
          chmod -R u+w $TMP/m2repo
          mvn -B package --offline -Dmaven.repo.local=$TMP/m2repo
          runHook postBuild
        '';

        installPhase = ''
          runHook preInstall
          mkdir -p $out/bin $out/lib
          cp target/torneo-de-los-inmortales-1.0.jar $out/lib/torneo-de-los-inmortales.jar
          makeWrapper ${pkgs.openjdk21}/bin/java $out/bin/torneo-de-los-inmortales \
            --add-flags "-cp $out/lib/torneo-de-los-inmortales.jar sigma.Main"
          runHook postInstall
        '';
      };

      apps.default = {
        type = "app";
        program = "${self.packages.${system}.default}/bin/torneo-de-los-inmortales";
      };

      devShells.default = pkgs.mkShell {
        buildInputs = [
          pkgs.openjdk21
          pkgs.maven
        ];
      };
    }
  );
}
