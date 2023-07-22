package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDao {

	// Criado uma dependencia q estará disponivel em qualquer lugar dentro dessa
	// classe
	private Connection conn;

	public SellerDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Seller obj) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(Seller obj) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteById(Integer id) {
		// TODO Auto-generated method stub

	}

	@Override
	public Seller findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.prepareStatement(
					"SELECT seller.*,department.Name as DepName\r\n"
					+ "FROM seller INNER JOIN department\r\n"
					+ "ON seller.DepartmentId = department.Id\r\n"
					+ "WHERE seller.Id = ?"
					);
			
			st.setInt(1, id); // O primeiro (1) ponto de interrogaçao recebera como paramento o id 
			rs = st.executeQuery();
			
			/**
			 * 			IMPORTANTE ENTENDIMENTO:
			 * 		
			 * 		O ResulSet, nos traz os dados em formato de TABELA sendo um objeto com Linhas e Colunas (igual aparece no Workbench.
			 * 		Só que como estamos progamando Orientado a Objetos, nos precisamos transformar esses dados em Objetos Associados !
			 * 		Objetos Associados: são objetos que tem uma associação com outro ( Seller <-> Department ).
			 * 		
			 * 		Entao como estamos tratando de OOP, precisamos ter na memória do computador, Objetos Associados INSTANCIADOS em memória.
			 * 
			 * 		O IF abaixo, serve pra testar se recebeu algum resultado referente ao ID recebido la no inicio do método.
			 * 		Se nao retonar nada, vai dar NULL e se retornar alguma informação (no caso as informações que constam na tabela),
			 * 		teremos que navegar por esse resultado pra podermos instanciarmos os Objetos (No caso abaixo, o Seller com Department)
			 * 		
			 * 		
			 */
			
			if (rs.next()) {
				Department dep = instantiateDepartment(rs);				
				Seller obj = instantiateSeller(rs, dep);
				
				return obj;
				
			}
			return null;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
		
	}

	private Seller instantiateSeller(ResultSet rs, Department dep) throws SQLException {
		Seller obj = new Seller();
		obj.setId(rs.getInt("Id"));
		obj.setName(rs.getString("Name"));
		obj.setEmail(rs.getString("Email"));
		obj.setBaseSalary(rs.getDouble("BaseSalary"));
		obj.setBirthDate(rs.getDate("BirthDate"));
		obj.setDepartment(dep);
		return obj;
	}

	private Department instantiateDepartment(ResultSet rs) throws SQLException {
		Department dep = new Department();
		dep.setId(rs.getInt("DepartmentId"));
		dep.setName(rs.getString("DepName"));
		return dep;
		
	}

	@Override
	public List<Seller> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

}
