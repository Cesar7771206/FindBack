    public void cargarDesdeTxt(){
        objetos.clear();
        File archivo = new File(RUTA_ARCHIVO);
        if(!archivo.exists()){
            crearArchivoVacio(archivo);
            return;
        }
        
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))){
            String linea;
            while ((linea=br.readLine())!=null){
                
                if (linea.trim().isEmpty()) continue;
                
                String[] p = linea.split("\\|");
                
                if (p.length>=10){
                    String tipo =p[0];
                    int id = Integer.parseInt(p[1]);
                    String nombre = p[2];
                    String descripcion = p[3];
                    String color = p[5];
                    String fecha = p[6];
                    String ubicacion = p[7];
                    String estado = p[8];
                    Estudiante reportadoPor = usuarioRepo.buscarPorCodigo(p[9]);
                    
                    ObjetoReportado obj=null;
                    
                    switch(tipo){
                        case "DOCUMENTO":
                            if (p.length>=13){
                                obj=new Documento( p[10], p[11], id, nombre, descripcion, "DOCUMENTO", color, fecha, ubicacion, estado, reportadoPor);
                               
                            }
                            break;
                        case "ELECTRONICO":
                            if(p.length>=13){
                                obj = new DispositivoElectronico(p[10], p[11], p[12], id, nombre, descripcion, "DISPOSITIVO ELECTRONICO", color, fecha, ubicacion, estado, reportadoPor);
                            }
                            break;
                        case "PRENDA":
                            if (p.length>=12){
                                obj = new Prenda(p[10], p[11], id, nombre, descripcion, "PRENDA", color, fecha, ubicacion, estado, reportadoPor);
                            }
                            break;
                        case "GENERICO":
                            if(p.length>=11){
                                 obj = new ObjetoGenerico(p[10], id, nombre, descripcion, categoria, color, fecha, ubicacion, estado, reportadoPor);
                            }
                            break;
                    }
                    
                    if (obj !=null){
                        objetos.add(obj);
                    }
                } 
            }
        }catch (IOException e){
                System.out.println("Error al leer objetos.txt: "+ e.getMessage());
        }
    }
